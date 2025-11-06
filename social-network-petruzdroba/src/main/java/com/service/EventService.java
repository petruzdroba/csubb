package main.java.com.service;

import main.java.com.containers.DuckRaceContainer;
import main.java.com.domain.Culoar;
import main.java.com.domain.Duck;
import main.java.com.domain.OptimiserResult;
import main.java.com.domain.RaceEvent;
import main.java.com.optimisers.BacktrackOptimiser;
import main.java.com.repo.UserRepository;

import java.util.Collection;

public class EventService{
    private final CardService cardService;
    private final UserRepository userRepository;
    private final RaceEvent raceEvent;

    public EventService(CardService cardService, UserRepository userRepository, RaceEvent raceEvent) {
        this.cardService = cardService;
        this.userRepository = userRepository;
        this.raceEvent = raceEvent;
    }

    public void subscribe(long userId){
        raceEvent.subscribe(userRepository.find(userId));
    }

    public void unsubscribe(long userId){
        raceEvent.unsubscribe(userRepository.find(userId));
    }

    public void startRace(Collection<Culoar> culoars){
        Collection<Duck> swimmers = cardService.getDucksInCard(Duck.TipRata.SWIMMING);
        DuckRaceContainer container = new DuckRaceContainer(swimmers, culoars);

        BacktrackOptimiser optimiser = new BacktrackOptimiser(container);
        OptimiserResult result = optimiser.findMinimumTime();

        raceEvent.setRaceResult(result);
        raceEvent.notifySubscribers();
    }
}
