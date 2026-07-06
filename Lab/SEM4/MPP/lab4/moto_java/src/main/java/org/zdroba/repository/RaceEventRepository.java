package org.zdroba.repository;

import org.zdroba.entity.RaceEvent;

import java.util.List;

public interface RaceEventRepository {

    List<RaceEvent> getAll();
    
    void add(RaceEvent raceEvent);

    RaceEvent find(int engine);
}
