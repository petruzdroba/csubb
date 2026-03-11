package org.zdroba.repository;

import java.util.List;

public interface IRepository<K,T> {
    void add(T entity);

    void delete(K key);

    void update(K key, T entity);

    T find(K key);

    List<T> getAll();

    List<K> getKeys();
}
