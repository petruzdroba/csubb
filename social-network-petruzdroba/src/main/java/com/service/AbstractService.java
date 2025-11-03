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

    public AbstractService(AbstractRepository<K, T> repository) {
        this.repository = repository;
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
