package org.zdroba.service;

import org.zdroba.entity.RaceEvent;
import org.zdroba.entity.Racer;
import org.zdroba.entity.Team;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.NotFoundException;
import org.zdroba.repository.RaceEventRepository;
import org.zdroba.repository.RaceEventRepositoryImpl;
import org.zdroba.repository.RacerRepository;
import org.zdroba.repository.RacerRepositoryImpl;
import org.zdroba.sync.*;

import java.util.ArrayList;
import java.util.List;

public class RacerServiceImpl implements RacerService, Observable {

    private final RacerRepository racerRepository;
    private final RaceEventRepository raceEventRepository;
    private final SocketNotifier notifier;
    private List<Observer> observers = new ArrayList<>();

    public RacerServiceImpl(RacerRepository racerRepository, RaceEventRepository raceEventRepository, SocketNotifier notifier) {
        this.racerRepository = racerRepository;
        this.raceEventRepository = raceEventRepository;
        this.notifier = notifier;
    }

    @Override
    public List<Racer> find(Team team) {
        return racerRepository.getBy(team);
    }

    @Override
    public List<Racer> getAll() {
        return racerRepository.getAll();
    }

    @Override
    public List<Racer> getAll(int engine) {
        RaceEvent raceEvent = raceEventRepository.find(engine);

        if (raceEvent == null)
            return List.of();
        return racerRepository.getBy(raceEvent);
    }

    @Override
    public void add(String nume, String cnp, Integer engine, Team team) throws AlreadyExistsException, NotFoundException {

        Racer racer = racerRepository.find(cnp);
        if(racer != null)
            throw new AlreadyExistsException(String.format("Racer with cnp: %s already exists",cnp));

        RaceEvent raceEvent = raceEventRepository.find(engine);
        if(raceEvent == null)
            throw new NotFoundException(String.format("Race event with engine: %d does not exist", engine));

        racer = new Racer(nume,cnp,team, raceEvent);
        racerRepository.add(racer); // id is auto generated and mutated in the object in the .add meth

        if (notifier != null) {
            Request req = new Request(RequestType.RACER_ADD, "{\"id\":" + racer.getId() + "}");
            notifier.notifyPeer(req.toString());
        }

        notify("RACER: ADD " + racer.getId());
    }

    @Override
    public void modify(Long id, Team team) throws NotFoundException {

        Racer racer = racerRepository.find(id);
        if(racer == null)
            throw new NotFoundException(String.format("Racer with id: %d was not found", id));

        racer.setTeam(team);
        racerRepository.modify(racer);

        if (notifier != null) {
            Request req = new Request(RequestType.RACER_UPDATE, "{\"id\":" + racer.getId() + "}");
            notifier.notifyPeer(req.toString());
        }
    }

    @Override
    public void add(Observer observer) {
        if(!observers.contains(observer))
            observers.add(observer);
    }

    @Override
    public void remove(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(String message) {
        for(Observer observer: observers){
            observer.update(message);
        }
    }
}
