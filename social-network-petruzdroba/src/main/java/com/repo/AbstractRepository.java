package com.repo;

import com.exceptions.RepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository abstract pentru stocarea obiectelor in memorie.
 *
 * @param <K> Tipul cheii obiectelor.
 * @param <T> Tipul obiectelor stocate.
 */
public abstract class AbstractRepository<K, T> {
    protected Map<K, T> data = new HashMap<>();

    /**
     * Returneaza toate obiectele din repository.
     *
     * @return Colectie cu toate obiectele stocate.
     */
    public Collection<T> getAll(){
        return data.values();
    }

    /**
     * Returneaza toate cheile obiectelor din repository.
     *
     * @return Colectie cu toate cheile obiectelor.
     */
    public Collection<K> getKeys(){
        return data.keySet();
    }

    /**
     * Adauga un obiect in repository.
     *
     * @param key Cheia obiectului.
     * @param newData Obiectul de adaugat.
     * @throws RepositoryException Daca cheia exista deja in repository.
     */
    public void add(K key, T newData) throws RepositoryException{
        if(data.containsKey(key))
            throw new RepositoryException("Data already exists");

        data.put(key, newData);
    }

    /**
     * Sterge un obiect din repository dupa cheia sa.
     *
     * @param key Cheia obiectului de sters.
     * @throws RepositoryException Daca cheia nu exista in repository.
     */
    public void remove(K key) throws RepositoryException{
        if(!data.containsKey(key))
            throw new RepositoryException("Key id dosent exist");
        data.remove(key);
    }

    /**
     * Modifica un obiect existent in repository.
     *
     * @param key Cheia obiectului de modificat.
     * @param newData Obiectul actualizat.
     * @throws RepositoryException Daca cheia nu exista in repository.
     */
    public void modify(K key, T newData) throws RepositoryException{
        if(!data.containsKey(key))
            throw new RepositoryException("Key id dosent exist");
        data.put(key, newData);
    }

    /**
     * Cauta un obiect dupa cheia sa.
     *
     * @param key Cheia obiectului cautat.
     * @return Obiectul corespunzator cheii sau null daca nu exista.
     */
    public T find(K key){
        return data.get(key);
    }
}
