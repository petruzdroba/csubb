package org.example.repo;

import java.util.List;

public interface RepositoryPaginated<K, T> extends Repository<K, T> {

    List<T> getPage(int limit, int offset);

    int pageCount(int pageSize);
}
