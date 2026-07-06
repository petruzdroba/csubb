using System.Collections.Generic;
using moto_c.Common.sync;
using moto_c.entity;
using moto_c.exceptions;
using moto_c.repository;
using moto_c.sync;

namespace moto_c.service;

public class RaceEventServiceImpl: RaceEventService
{
    private RaceEventRepository raceEventRepository;
    private readonly SocketNotifier notifier;

    public RaceEventServiceImpl(RaceEventRepository raceEventRepository, SocketNotifier notifier)
    {
        this.raceEventRepository = raceEventRepository;
        this.notifier = notifier;
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

        Request req = new Request(RequestType.EVENT_ADD, "{\"id\":" + raceEvent.id + "}");
        notifier.NotifyPeer(req.ToString());
    }
}