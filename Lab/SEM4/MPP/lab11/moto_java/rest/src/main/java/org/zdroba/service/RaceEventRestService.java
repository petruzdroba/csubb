package org.zdroba.service;

import org.zdroba.entity.RaceEvent;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.InvalidEngineException;
import org.zdroba.exceptions.NotFoundException;
import org.zdroba.repository.RaceEventRepository;

import java.util.List;

public class RaceEventRestService {

    private final RaceEventRepository repository;

    public RaceEventRestService(RaceEventRepository repository) {
        this.repository = repository;
    }


    public RaceEvent save(int engine) throws AlreadyExistsException, InvalidEngineException {
        if(repository.find(engine) != null)
            throw new AlreadyExistsException("Race with this engine exists");

        if(engine <= 0)
            throw new InvalidEngineException("Engine value cannot be lower than 0");

        RaceEvent e = new RaceEvent(engine);
        repository.add(e);

        return e;
    }

    public List<RaceEvent> findAll() {
        return repository.getAll();
    }

    public RaceEvent findById(Long id) throws NotFoundException {
        RaceEvent e = repository.find(id);
        if(e == null)
            throw new NotFoundException("Race event with this id dosnet exist");

        return e;
    }

    public RaceEvent update(Long id, int engine)
            throws NotFoundException, AlreadyExistsException, InvalidEngineException {

        RaceEvent e = repository.find(id);
        if (e == null)
            throw new NotFoundException("Race event with this id doesn't exist");

        if (engine <= 0)
            throw new InvalidEngineException("Engine value cannot be lower than 0");

        RaceEvent existing = repository.find(engine);
        if (existing != null && !existing.getId().equals(id)) {
            throw new AlreadyExistsException("Race with this engine exists");
        }

        e.setEngine(engine);
        repository.update(e);

        return e;
    }

    public void delete(Long id) throws NotFoundException {
        RaceEvent e = repository.find(id);
        if(e == null)
            throw new NotFoundException("Race event with this id dosnet exist");

        repository.delete(id);
    }

    public RaceEvent filter(int engine) throws NotFoundException{
        RaceEvent e = repository.find(engine);
        if(e == null)
            throw new NotFoundException("Race event with this engine dosent exist");

        return e;
    }
}
