package org.zdroba.repository;

import org.zdroba.entity.RaceEvent;
import org.zdroba.entity.Racer;
import org.zdroba.entity.Team;

import java.util.List;

public interface RacerRepository {
    Racer find(Long id);

    List<Racer> getAll();

    List<Racer> getBy(Team team);

    List<Racer> getBy(RaceEvent engine);

    void add(Racer racer);

    void modify(Racer racer);
}
