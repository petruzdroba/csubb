package org.zdroba.service;

import org.zdroba.entity.RaceEvent;
import org.zdroba.entity.Racer;
import org.zdroba.entity.Team;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.NotFoundException;

import java.util.List;

public interface RacerService {

    List<Racer> find(Team team);

    List<Racer> getAll();

    List<Racer> getAll(int engine);

    void add(String nume, String cnp, Integer engine, Team team) throws AlreadyExistsException, NotFoundException;

    void modify(Long id, Team team) throws NotFoundException;
}
