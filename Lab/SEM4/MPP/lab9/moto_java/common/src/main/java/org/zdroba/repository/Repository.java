package org.zdroba.repository;

import java.util.List;

public interface Repository <K,T>{

    T find(K key);

    List<T> getAll();
}
