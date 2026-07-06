package org.zdroba.repository;

import org.zdroba.entity.RaceEvent;

public interface RaceEventRepository extends Repository<Long, RaceEvent>{

    void add(RaceEvent raceEvent);

    RaceEvent find(int engine);
}
