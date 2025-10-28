package main.java.com.service;

import main.java.com.repo.AbstractRepository;
import main.java.com.validators.Validator;

public abstract class AbstractService<K, T> {
    protected final AbstractRepository<K, T> repository;

    public AbstractService(AbstractRepository<K, T> repository) {
        this.repository = repository;
    }
}
