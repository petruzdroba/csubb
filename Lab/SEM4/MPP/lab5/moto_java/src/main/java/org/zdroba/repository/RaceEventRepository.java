package org.zdroba.repository;

import org.zdroba.entity.RaceEvent;

import java.util.List;

public interface RaceEventRepository extends Repository<Long, RaceEvent>{

    void add(RaceEvent raceEvent);

    RaceEvent find(int engine);
}
