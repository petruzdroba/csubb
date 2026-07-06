using moto_c.entity;

namespace moto_c.service;

public interface RaceEventService
{
    List<RaceEvent> getAll();

    void add(int engine);
}