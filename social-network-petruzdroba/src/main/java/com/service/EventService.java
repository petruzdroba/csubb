package main.java.com.service;

import main.java.com.containers.DuckRaceContainer;
import main.java.com.domain.Culoar;
import main.java.com.domain.Duck;
import main.java.com.domain.Event;
import main.java.com.domain.RaceEvent;
import main.java.com.exceptions.ValidationException;
import main.java.com.repo.AbstractRepository;
import main.java.com.repo.UserRepository;
import main.java.com.validators.CuloarValidator;
import main.java.com.validators.EventValidator;

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

    public void add(long id, Collection<Culoar> culoars) throws ValidationException {
        for(Culoar c: culoars)
            culoarValidator.validateThrow(c);

        Collection<Duck> swimmers = cardService.getDucksInCard(Duck.TipRata.SWIMMING);
        DuckRaceContainer container = new DuckRaceContainer(swimmers, culoars);

        RaceEvent event = new RaceEvent(id, container);
        eventValidator.validateThrow(event);

        repository.add(id, event);
    }

    public void remove(long id) throws ValidationException {
        if(id < 0)
            throw new ValidationException("Event id cannot be negative\n");
        repository.remove(id);
    }

    public void subscribe(long eventId, long userId){
        repository.find(eventId).subscribe(userRepository.find(userId));
    }

    public void unsubscribe(long eventId, long userId){
        repository.find(eventId).unsubscribe(userRepository.find(userId));
    }

    public void startRace(long raceId){
        Event event = repository.find(raceId);
        repository.remove(raceId);

        event.start();
        event.notifySubscribers();
    }
}
