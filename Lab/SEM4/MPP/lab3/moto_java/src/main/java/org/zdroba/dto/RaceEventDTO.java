package org.zdroba.dto;

import org.zdroba.entity.RaceEvent;

import java.util.List;

public interface RaceEventDTO {

    List<RaceEvent> getAll();
    
    void add(RaceEvent raceEvent);
}
