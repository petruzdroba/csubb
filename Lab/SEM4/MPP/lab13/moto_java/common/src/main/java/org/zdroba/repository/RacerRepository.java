package org.zdroba.repository;

import org.zdroba.entity.RaceEvent;
import org.zdroba.entity.Racer;
import org.zdroba.entity.Team;

import java.util.List;

public interface RacerRepository extends Repository<Long, Racer> {
    Racer find(String cnp);

    List<Racer> getBy(Team team);

    List<Racer> getBy(RaceEvent engine);

    void add(Racer racer);

    void modify(Racer racer);
}
