package org.zdroba.service;

import org.zdroba.entity.RaceEvent;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.repository.RaceEventRepository;
import org.zdroba.repository.RaceEventRepositoryImpl;
import org.zdroba.sync.Request;
import org.zdroba.sync.RequestType;
import org.zdroba.sync.SocketNotifier;

import java.util.List;

public class RaceEventServiceImpl implements RaceEventService{

    private final RaceEventRepository raceEventRepository;
    private final SocketNotifier notifier;


    public RaceEventServiceImpl(RaceEventRepository raceEventRepository, SocketNotifier notifier) {
        this.raceEventRepository = raceEventRepository;
        this.notifier = notifier;
    }

    @Override
    public List<RaceEvent> getAll() {
        return raceEventRepository.getAll();
    }

    @Override
    public void add(int engine) throws AlreadyExistsException {
        RaceEvent raceEvent = raceEventRepository.find(engine);

        if(raceEvent!=null)
            throw new AlreadyExistsException(String.format("An event with engine: %d already exists", engine));

        raceEvent = new RaceEvent(engine);
        raceEventRepository.add(raceEvent); // object gets id returned in .add method

        if (notifier != null) {
            Request req = new Request(RequestType.EVENT_ADD, "{\"id\":" + raceEvent.getId() + "}");
            notifier.notifyPeer(req.toString());
        }
    }
}
