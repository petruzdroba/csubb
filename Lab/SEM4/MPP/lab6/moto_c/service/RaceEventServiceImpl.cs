using moto_c.entity;
using moto_c.exceptions;
using moto_c.repository;

namespace moto_c.service;

public class RaceEventServiceImpl: RaceEventService
{
    private RaceEventRepository raceEventRepository;

    public RaceEventServiceImpl(RaceEventRepository raceEventRepository)
    {
        this.raceEventRepository = raceEventRepository;
    }

    public List<RaceEvent> getAll()
    {
        return raceEventRepository.getAll();
    }

    public void add(int engine)
    {
        RaceEvent? raceEvent = raceEventRepository.find(engine);

        if (raceEvent != null)
            throw new AlreadyExistsException("Event with this engine exists");

        raceEvent = new RaceEvent(engine);
        raceEventRepository.add(raceEvent);
    }
}