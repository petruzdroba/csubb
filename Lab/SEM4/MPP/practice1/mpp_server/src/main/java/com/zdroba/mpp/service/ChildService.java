package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Child;
import com.zdroba.mpp.exceptions.NotFoundException;
import com.zdroba.mpp.repository.IChildRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChildService implements IChildService{

    private final IChildRepository repository;

    public ChildService(IChildRepository repository) {
        this.repository = repository;
    }

    @Override
    public Child update(Long childId, Long checkpointId, Instant time) throws NotFoundException {
        Child existing = repository.get(childId);

        if(existing == null)
            throw new NotFoundException("Child with this id dosent exist");

        existing.setCheckpointId(checkpointId);
        existing.setHour(time);

        repository.update(existing);
        return existing;
    }

    @Override
    public List<Child> get() {
        return repository.getAll();
    }

    @Override
    public List<Child> getCheck(Long checkpointId) {
        return repository.getCheck(checkpointId);
    }

    @Override
    public Child get(Long id) {
        return repository.get(id);
    }
}
