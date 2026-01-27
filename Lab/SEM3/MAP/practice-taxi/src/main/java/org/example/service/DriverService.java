package org.example.service;

import org.example.domain.Driver;
import org.example.repo.DriverRepository;

import java.util.List;

public class DriverService implements DriverServiceI{

    private final DriverRepository repository;

    public DriverService(DriverRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Driver> getAll() {
        return repository.getAll();
    }
}
