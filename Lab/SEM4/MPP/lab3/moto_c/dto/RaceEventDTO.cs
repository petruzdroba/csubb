using moto_c.entity;

namespace moto_c.dto;

public interface RaceEventDTO
{
    List<RaceEvent> getAll();
    
    void add(RaceEvent raceEvent);
}