package org.zdroba.service;

import org.zdroba.entity.RaceEvent;
import org.zdroba.exceptions.AlreadyExistsException;

import java.util.List;

public interface RaceEventService {

    List<RaceEvent> getAll();

    void add(int engine) throws AlreadyExistsException;
}
