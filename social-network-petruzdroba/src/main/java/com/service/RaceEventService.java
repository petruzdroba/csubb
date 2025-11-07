package main.java.com.service;

import main.java.com.containers.DuckRaceContainer;
import main.java.com.domain.Culoar;
import main.java.com.domain.Duck;
import main.java.com.domain.OptimiserResult;
import main.java.com.domain.RaceEvent;
import main.java.com.exceptions.DomainException;
import main.java.com.exceptions.ValidationException;
import main.java.com.optimisers.BacktrackOptimiser;
import main.java.com.repo.UserRepository;
import main.java.com.validators.CuloarValidator;

import java.util.Collection;

public class RaceEventService {
    private final CardService cardService;
    private final UserRepository userRepository;
    private final RaceEvent raceEvent;
    private final CuloarValidator culoarValidator = new CuloarValidator();

    public RaceEventService(CardService cardService, UserRepository userRepository, RaceEvent raceEvent) {
        this.cardService = cardService;
        this.userRepository = userRepository;
        this.raceEvent = raceEvent;
    }

    public void subscribe(long userId) throws DomainException {
        raceEvent.subscribe(userRepository.find(userId));
    }

    public void unsubscribe(long userId) throws DomainException {
        raceEvent.unsubscribe(userRepository.find(userId));
    }

    public void startRace(Collection<Culoar> culoars) throws ValidationException {
        for(Culoar c: culoars)
            culoarValidator.validateThrow(c);

        Collection<Duck> swimmers = cardService.getDucksInCard(Duck.TipRata.SWIMMING);
        DuckRaceContainer container = new DuckRaceContainer(swimmers, culoars);

        BacktrackOptimiser optimiser = new BacktrackOptimiser(container);
        OptimiserResult result = optimiser.findMinimumTime();

        raceEvent.setRaceResult(result);
        raceEvent.notifySubscribers();
    }
}
