package main.java.com.repo;

import main.java.com.exceptions.RepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRepository<K, T> {
    protected Map<K, T> data = new HashMap<>();

    public Collection<T> getAll(){
        return data.values();
    }

    public void add(K key, T newData) throws RepositoryException{
        if(data.containsKey(key))
            throw new RepositoryException("Data already exists");

        data.put(key, newData);
    }

    public void remove(K key) throws RepositoryException{
        if(!data.containsKey(key))
            throw new RepositoryException("Key id dosent exist");
        data.remove(key);
    }

    public void modify(K key, T newData) throws RepositoryException{
        if(!data.containsKey(key))
            throw new RepositoryException("Key id dosent exist");
        data.put(key, newData);
    }

    public T find(K key){
        return data.get(key);
    }
}
