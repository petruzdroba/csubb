package main.java.com.service;

import main.java.com.repo.AbstractRepository;

import java.util.Collection;

public abstract class AbstractService<K, T> {
    protected final AbstractRepository<K, T> repository;
    protected final AbstractRepository<?, ?> dependencyRepository;

    public AbstractService(AbstractRepository<K, T> repository) {
        this.repository = repository;
        this.dependencyRepository = null;
    }

    public AbstractService(AbstractRepository<K, T> repository, AbstractRepository<?, ?> dependencyRepository) {
        this.repository = repository;
        this.dependencyRepository = dependencyRepository;
    }

    public Collection<T> getAll(){
        return repository.getAll();
    }
}
