package org.example.repo;

import java.util.List;

public interface Repository <K, T>{

    void add(K key, T entity);

    T find(K key);

    void delete(K key);

    void update(K key, T entity);

    List<T> getAll();

    List<K> getKeys();
}
