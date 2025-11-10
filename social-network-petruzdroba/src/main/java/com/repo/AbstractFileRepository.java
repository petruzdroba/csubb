package com.repo;

import com.exceptions.RepositoryException;

/**
 * Repository abstract pentru stocarea obiectelor in fisier.
 *
 * Extinde {@link AbstractRepository} si suprascrie metodele de adaugare, stergere
 * si modificare pentru a actualiza automat fisierul dupa fiecare operatie.
 *
 * @param <K> Tipul cheii obiectelor.
 * @param <T> Tipul obiectelor stocate.
 */
public abstract class AbstractFileRepository<K,T> extends AbstractRepository<K, T>{
    protected final String filePath;

    public AbstractFileRepository(String filePath) {
        this.filePath = filePath;
        loadFile();
    }

    protected abstract void loadFile();

    protected abstract void overwriteFile();

    @Override
    public void add(K key, T newData) throws RepositoryException {
        super.add(key, newData);
        overwriteFile();
    }

    @Override
    public void remove(K key) throws RepositoryException{
        super.remove(key);
        overwriteFile();
    }

    @Override
    public void modify(K key, T newData) throws RepositoryException{
        super.modify(key, newData);
        overwriteFile();
    }
}
