package com.service;

import com.containers.DuckRaceContainer;
import com.domain.*;
import com.exceptions.DomainException;
import com.exceptions.NotLoggedIn;
import com.exceptions.ValidationException;
import com.repo.AbstractDatabaseRepository;
import com.repo.RaceEventRepository;
import com.repo.UserRepository;
import com.validators.CuloarValidator;
import com.validators.EventValidator;

import java.sql.SQLException;
import java.util.Collection;

public class RaceEventService extends AbstractService<Long, RaceEvent>{
    private final UserRepository userRepository;
    private final CardService cardService;
    private final CuloarValidator culoarValidator = new CuloarValidator();

    public RaceEventService(AbstractDatabaseRepository<Long, RaceEvent> repository, UserRepository userRepository, CardService cardService) {
        super(repository);
        this.userRepository = userRepository;
        this.cardService = cardService;
    }

    public void add(User currentUser, Collection<Culoar> lanes) throws SQLException, ValidationException {
        if (currentUser == null)
            throw new NotLoggedIn("User must be logged in");

        if (lanes == null || lanes.isEmpty())
            throw new ValidationException("Event must have lanes");

        lanes.forEach(culoarValidator::validateThrow);

        Collection<Duck> swimmers = cardService.getDucksInCard(Duck.TipRata.SWIMMING);
        DuckRaceContainer container = new DuckRaceContainer(swimmers, lanes);

        RaceEvent event = new RaceEvent(-1,currentUser.getId(), container);

        repository.add(null, event);
    }

    public void remove(User currentUser, long eventId) throws SQLException, ValidationException {
        if (currentUser == null)
            throw new NotLoggedIn("User must be logged in");

        RaceEvent event = repository.find(eventId);
        if (event == null)
            throw new ValidationException("Event not found");

        if (event.getOwnerId() != currentUser.getId())
            throw new ValidationException("Only the owner can delete this event");

        repository.remove(eventId);
    }

    public void subscribe(User currentUser, long eventId) throws SQLException, ValidationException {
        if (currentUser == null)
            throw new NotLoggedIn("User must be logged in");

        RaceEvent event = repository.find(eventId);
        if (event == null)
            throw new ValidationException("Event not found");

        ((RaceEventRepository) repository).subscribe(eventId, currentUser);
    }

    public void unsubscribe(User currentUser, long eventId) throws SQLException, ValidationException {
        if (currentUser == null)
            throw new NotLoggedIn("User must be logged in");

        RaceEvent event = repository.find(eventId);
        if (event == null)
            throw new ValidationException("Event not found");

        ((RaceEventRepository) repository).unsubscribe(eventId, currentUser);
    }

    public void startRace(User currentUser,long eventId) throws SQLException {
        RaceEvent event = repository.find(eventId);
        if (event == null)
            throw new ValidationException("Event not found");

        if(event.getOwnerId() != currentUser.getId())
            throw new DomainException("Only owner can start events");

        repository.remove(eventId);
        event.start();
        event.notifySubscribers();
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
}
