package main.java.com.service;

import main.java.com.repo.AbstractRepository;

import java.util.Collection;

/**
 * Service abstract pentru procedurile cu  obiectele
 *
 * @param <K> Tipul cheii obiectelor.
 * @param <T> Tipul obiectelor stocate.
 */
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

    /**
     * Returneaza toate elementele T din Repository
     * <p>
     * @return o colectie ce contine toate elementele de tip T din Repository
     * @see main.java.com.repo.AbstractRepository#getAll()
     */
    public Collection<T> getAll(){
        return repository.getAll();
    }
}
