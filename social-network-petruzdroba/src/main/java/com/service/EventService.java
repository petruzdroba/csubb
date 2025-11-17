package com.service;

import com.containers.DuckRaceContainer;
import com.domain.*;
import com.exceptions.DomainException;
import com.exceptions.RepositoryException;
import com.exceptions.ValidationException;
import com.repo.AbstractRepository;
import com.repo.EventRepository;
import com.repo.UserRepository;
import com.validators.CuloarValidator;
import com.validators.EventValidator;

import java.sql.SQLException;
import java.util.Collection;

public class EventService extends AbstractService<Long, Event>{
    private final EventValidator<Event> eventValidator = new EventValidator<Event>();
    private final CardService cardService;
    private final UserRepository userRepository;
    private final CuloarValidator culoarValidator = new CuloarValidator();


    public EventService(AbstractRepository<Long, Event> repository, CardService cardService, UserRepository userRepository) {
        super(repository);
        this.cardService = cardService;
        this.userRepository = userRepository;
    }

    /**
     * Adauga un nou eveniment {@link RaceEvent} cu un set de culoare {@link Culoar}.
     * <p>
     * Creeaza containerul {@link DuckRaceContainer} folosind ratele de tip {@link Duck.TipRata#SWIMMING}.
     * Valideaza culoarele si evenimentul inainte de adaugare.
     *
     * @param id       Identificatorul unic al evenimentului
     * @param culoars  Colectia de {@link Culoar} asociate evenimentului
     * @throws ValidationException Daca orice culoar sau eveniment nu trece validarea
     * @see DuckRaceContainer
     * @see RaceEvent
     * @see CuloarValidator
     * @see EventValidator
     */
    public void add(long id, Collection<Culoar> culoars) throws ValidationException {
        culoars.forEach(culoarValidator::validateThrow);

        Collection<Duck> swimmers = cardService.getDucksInCard(Duck.TipRata.SWIMMING);
        DuckRaceContainer container = new DuckRaceContainer(swimmers, culoars);

        RaceEvent event = new RaceEvent(id, container);
        eventValidator.validateThrow(event);

        repository.add(id, event);
    }


    /**
     * Sterge un eveniment existent dupa id.
     *
     * @param id Identificatorul evenimentului
     * @throws ValidationException Daca id-ul este negativ
     */
    public void remove(long id) throws ValidationException {
        if(id < 0)
            throw new ValidationException("Event id cannot be negative\n");
        repository.remove(id);
    }

    /**
     * Inscrie un utilizator {@link User} la un eveniment {@link Event}.
     *
     * @param eventId Id-ul evenimentului
     * @param userId  Id-ul utilizatorului
     * @throws DomainException Daca utilizatorul este deja inscris la eveniment
     * @see Event#subscribe(User)
     */
    public void subscribe(long eventId, long userId) throws RepositoryException {
        try {
            if(repository instanceof EventRepository)
                ((EventRepository) repository).subscribe(eventId, userRepository.find(userId));
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }



    /**
     * Dezaboneaza un utilizator {@link User} de la un eveniment {@link Event}.
     *
     * @param eventId Id-ul evenimentului
     * @param userId  Id-ul utilizatorului
     * @throws DomainException Daca utilizatorul nu este inscris la eveniment
     * @see Event#unsubscribe(User)
     */
    public void unsubscribe(long eventId, long userId) throws DomainException {
        try {
            if(repository instanceof EventRepository)
                ((EventRepository) repository).unsubscribe(eventId, userRepository.find(userId));
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * Porneste cursa asociata unui eveniment {@link RaceEvent}, notifica toti utilizatorii inscrisi
     * si elimina evenimentul din sistem, deoarece cursa a fost incheiata.
     *
     * @param raceId Id-ul evenimentului/cursei
     * @see RaceEvent#start()
     * @see Event#notifySubscribers()
     */
    public void startRace(long raceId){
        Event event = repository.find(raceId);
        repository.remove(raceId);

        event.start();
        event.notifySubscribers();
    }

    /**
     * Returneaza toate evenimentele {@link Event} existente in repository.
     *
     * @return Colectie cu toate evenimentele
     */
    public Collection<Event> getAll(){
        return repository.getAll();
    }
}
