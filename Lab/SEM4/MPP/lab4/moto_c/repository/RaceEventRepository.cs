using moto_c.entity;

namespace moto_c.repository;

public interface RaceEventRepository
{
    List<RaceEvent> getAll();
    
    void add(RaceEvent raceEvent);

    RaceEvent? find(int engine);
}