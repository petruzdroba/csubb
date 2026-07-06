package org.zdroba.service;

import org.zdroba.entity.RaceEvent;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.repository.RaceEventRepository;
import org.zdroba.repository.RaceEventRepositoryImpl;

import java.util.List;

public class RaceEventServiceImpl implements RaceEventService{

    private final RaceEventRepository raceEventRepository;

    public RaceEventServiceImpl(RaceEventRepository raceEventRepository) {
        this.raceEventRepository = raceEventRepository;
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
    }
}
