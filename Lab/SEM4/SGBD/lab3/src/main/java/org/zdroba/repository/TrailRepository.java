package org.zdroba.repository;

import org.zdroba.entity.Trail;

import java.util.List;

public class TrailRepository implements ITrailRepository{

    private static TrailRepository instance;

    private TrailRepository() {}

    public static TrailRepository getInstance(){
        if(instance == null)
            instance = new TrailRepository();
        return instance;
    }

    @Override
    public void add(Trail entity) {

    }

    @Override
    public void delete(Long key) {

    }

    @Override
    public void update(Long key, Trail entity) {

    }

    @Override
    public Trail find(Long key) {
        return null;
    }

    @Override
    public List<Trail> getAll() {
        return List.of();
    }

    @Override
    public List<Long> getKeys() {
        return List.of();
    }
}
