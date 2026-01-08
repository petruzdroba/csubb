package com.service;

import com.containers.DuckRaceContainer;
import com.domain.*;
import com.exceptions.DomainException;
import com.exceptions.NotLoggedIn;
import com.exceptions.ValidationException;
import com.repo.AbstractDatabaseRepository;
import com.repo.RaceEventRepository;
import com.validators.CuloarValidator;
import javafx.application.Platform;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RaceEventService extends AbstractService<Long, RaceEvent> {
    private final CardService cardService;
    private final ExecutorService executor;
    private final CuloarValidator culoarValidator = new CuloarValidator();

    public RaceEventService(AbstractDatabaseRepository<Long, RaceEvent> repository, CardService cardService) {
        super(repository);
        this.cardService = cardService;
        this.executor = Executors.newFixedThreadPool(4);
    }

    public CompletableFuture<Void> add(User currentUser, Collection<Culoar> lanes) throws CompletionException {
        return CompletableFuture.runAsync(() -> {
            try {
                if (currentUser == null)
                    throw new NotLoggedIn("User must be logged in");

                if (lanes == null || lanes.isEmpty())
                    throw new ValidationException("Event must have lanes");

                lanes.forEach(culoarValidator::validateThrow);

                Collection<Duck> swimmers = cardService.getDucksInCard(Duck.TipRata.SWIMMING);
                DuckRaceContainer container = new DuckRaceContainer(swimmers, lanes);

                RaceEvent event = new RaceEvent(currentUser.getId(), currentUser.getId(), container);
                event.subscribe(currentUser);

                repository.add(null, event);

                pushObserver(this.observers.stream()
                        .filter(User.class::isInstance)
                        .map(User.class::cast)
                        .toList());
            } catch (SQLException | ValidationException | NotLoggedIn e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    public CompletableFuture<Void> remove(User currentUser, long eventId) throws CompletionException {
        return CompletableFuture.runAsync(() -> {
            try {
                if (currentUser == null)
                    throw new NotLoggedIn("User must be logged in");

                RaceEvent event = repository.find(eventId);
                if (event == null)
                    throw new ValidationException("Event not found");

                if (event.getOwnerId() != currentUser.getId())
                    throw new ValidationException("Only the owner can delete this event");

                repository.remove(eventId);
                pushObserver(this.observers.stream().filter(User.class::isInstance).map(User.class::cast).toList());
            } catch (SQLException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    public CompletableFuture<Void> subscribe(User currentUser, long eventId) {
        return CompletableFuture.runAsync(() -> {
            try {
                if (currentUser == null)
                    throw new NotLoggedIn("User must be logged in");

                RaceEvent event = repository.find(eventId);
                if (event == null)
                    throw new ValidationException("Event not found");

                ((RaceEventRepository) repository).subscribe(eventId, currentUser);

                pushObserver(List.of(currentUser));
            } catch (SQLException | ValidationException | NotLoggedIn e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    public CompletableFuture<Void> unsubscribe(User currentUser, long eventId) {
        return CompletableFuture.runAsync(() -> {
            try {
                if (currentUser == null)
                    throw new NotLoggedIn("User must be logged in");

                RaceEvent event = repository.find(eventId);
                if (event == null)
                    throw new ValidationException("Event not found");

                ((RaceEventRepository) repository).unsubscribe(eventId, currentUser);

                pushObserver(List.of(currentUser));
            } catch (SQLException | ValidationException | NotLoggedIn e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    public CompletableFuture<Void> startRace(User currentUser, long eventId) {
        return CompletableFuture.runAsync(() -> {
            try {
                RaceEvent event = repository.find(eventId);
                if (event == null)
                    throw new ValidationException("Event not found");

                if (event.getOwnerId() != currentUser.getId())
                    throw new DomainException("Only owner can start events");

                repository.remove(eventId);
                event.start();

                pushObserver(event, event.getSubscribers());
            } catch (SQLException | DomainException | ValidationException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }


    public Collection<RaceEvent> getAll() throws NotLoggedIn {
        return repository.getAll();
    }

    public Collection<RaceEvent> getPage(int offset, int limit) throws ValidationException {

        if (offset < 0 || limit < 1)
            throw new ValidationException("Invalid pagination parameters");

        return repository.getPage(offset, limit);
    }

    public Collection<Long> getKeys() throws NotLoggedIn {

        return repository.getKeys();
    }

    public int pageCount(int pageSize) throws ValidationException, NotLoggedIn {

        if (pageSize < 1)
            throw new ValidationException("Page size must be >= 1");

        return repository.pageCount(pageSize);
    }

    private void pushObserver(List<User> users) {
        CompletableFuture.runAsync(() -> {
            List<Long> notifiedIds = new ArrayList<>(users.stream()
                    .map(User::getId)
                    .toList());

            observers.stream()
                    .filter(User.class::isInstance)
                    .map(User.class::cast)
                    .filter(o -> notifiedIds.contains(o.getId()))
                    .forEach(o -> Platform.runLater(o::update));
        }, executor);
    }

    private void pushObserver(RaceEvent event, List<User> users) {
        CompletableFuture.runAsync(() -> {
            List<Long> notifiedIds = new ArrayList<>(users.stream()
                    .map(User::getId)
                    .toList());

            observers.stream()
                    .filter(User.class::isInstance)
                    .map(User.class::cast)
                    .filter(o -> notifiedIds.contains(o.getId()))
                    .forEach(o -> Platform.runLater(() -> o.notify(event)));
        }, executor);
    }
}
