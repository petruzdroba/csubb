using moto_c.entity;

namespace moto_c.repository;

public interface RaceEventRepository:Repository<long,RaceEvent>
{
    void add(RaceEvent raceEvent);

    RaceEvent? find(int engine);
}