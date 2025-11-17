package com.repo;

import com.domain.*;

import java.sql.SQLException;
import java.util.*;

public class EventRepository extends AbstractDatabaseRepository<Long, Event> {
    public EventRepository(String url, String user, String password) {
        super(url, user, password);
    }

    @Override
    public void add(Long key, Event entity) throws SQLException {

    }

    @Override
    public void remove(Long key) throws SQLException {

    }

    @Override
    public void modify(Long key, Event entity) throws SQLException {

    }

    @Override
    public Event find(Long key) throws SQLException {
        return null;
    }

    @Override
    public Collection<Event> getAll() {
        return List.of();
    }

    @Override
    public Collection<Long> getKeys() {
        return List.of();
    }
}
