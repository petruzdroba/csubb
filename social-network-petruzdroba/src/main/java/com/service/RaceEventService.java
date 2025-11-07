package main.java.com.service;

import main.java.com.containers.DuckRaceContainer;
import main.java.com.domain.*;
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

    /**
     * Aboneaza un utilizator la cursa.
     *
     * @param userId id-ul utilizatorului care se inscrie
     * @throws DomainException daca utilizatorul  nu poate fi inscris
     * @throws main.java.com.exceptions.RepositoryException daca utilizatorul nu exista
     *
     *
     * @see RaceEvent#subscribe(User)
     */
    public void subscribe(long userId) throws DomainException {
        raceEvent.subscribe(userRepository.find(userId));
    }

    /**
     * Dezaboneaza un utilizator de la cursa cursa.
     *
     * @param userId id-ul utilizatorului care se retrage
     * @throws DomainException daca utilizatorul nu era inscris
     * @throws main.java.com.exceptions.RepositoryException daca utilizatorul nu exista
     *
     * @see RaceEvent#unsubscribe(User)
     */
    public void unsubscribe(long userId) throws DomainException {
        raceEvent.unsubscribe(userRepository.find(userId));
    }

    /**
     * Porneste cursa.
     * <p>
     * Valideaza culoarele, selecteaza toate ratele de tip {@link Duck.TipRata#SWIMMING}
     * si optimizeaza alocarea ratelor pe culoare folosind {@link BacktrackOptimiser}.
     * Rezultatul este salvat in {@link RaceEvent} si se notifica toti abonatii.
     *
     * @param culoars colectia de culoare ce vor fi folosite in cursa
     *
     * @throws ValidationException daca cel putin un culoar este invalid
     *
     * @see DuckRaceContainer
     * @see BacktrackOptimiser
     * @see RaceEvent#setRaceResult(OptimiserResult)
     * @see RaceEvent#notifySubscribers()
     */
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
