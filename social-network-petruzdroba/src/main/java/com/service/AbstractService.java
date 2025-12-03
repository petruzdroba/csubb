package com.service;

import com.exceptions.RepositoryException;
import com.repo.AbstractDatabaseRepository;
import com.repo.AbstractRepository;

import java.util.Collection;

/**
 * Service abstract pentru procedurile cu  obiectele
 *
 * @param <K> Tipul cheii obiectelor.
 * @param <T> Tipul obiectelor stocate.
 */
public abstract class AbstractService<K, T> {
    protected final AbstractDatabaseRepository<K, T> repository;

    public AbstractService(AbstractDatabaseRepository<K, T> repository) {
        this.repository = repository;
    }


    /**
     * Returneaza toate elementele T din Repository
     * <p>
     * @return o colectie ce contine toate elementele de tip T din Repository
     * @see com.repo.AbstractDatabaseRepository#getAll()
     */
    public Collection<T> getAll(){
        return repository.getAll();
    }

    public Collection<T> getPage(int offset, int limit) {return  repository.getPage(offset, limit); }

    public int pageCount(int pageSize) throws RepositoryException {
        if(pageSize < 1)
            throw new RepositoryException("Page Size cannot be negative");

        return repository.pageCount(pageSize);
    }
}
