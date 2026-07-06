using System.Collections.Generic;
using moto_c.Common.sync;
using moto_c.entity;
using moto_c.exceptions;
using moto_c.repository;
using moto_c.sync;

namespace moto_c.service;

public class RacerServiceImpl : RacerService, Observable
{
    private readonly RacerRepository racerRepository;
    private readonly RaceEventRepository raceEventRepository;
    private readonly SocketNotifier notifier;
    private List<Observer> observers = new List<Observer>();

    public RacerServiceImpl(RacerRepository racerRepository, RaceEventRepository raceEventRepository, SocketNotifier notifier)
    {
        this.racerRepository = racerRepository;
        this.raceEventRepository = raceEventRepository;
        this.notifier = notifier;
    }

    public List<Racer> find(Team team)
    {
        return racerRepository.findBy(team);
    }

    public List<Racer> getAll()
    {
        return racerRepository.getAll();
    }

    public List<Racer> getAll(int engine)
    {
        RaceEvent? raceEvent = raceEventRepository.find(engine);

        if (raceEvent == null)
            return new List<Racer>();

        return racerRepository.findBy(raceEvent);
    }

    public void add(string nume, string cnp, int engine, Team team)
    {
        RaceEvent? raceEvent = raceEventRepository.find(engine);
        if (raceEvent == null)
            throw new NotFoundException(string.Format("Race event with engine: {0} does not exist", engine));

        Racer racer = new Racer(nume, cnp, team, raceEvent);
        racerRepository.add(racer);
        
        Request req = new Request(RequestType.RACER_ADD, "{\"id\":" + racer.id + "}");
        notifier.NotifyPeer(req.ToString());
        
        notify("RACER: ADD " + racer.id);
    }

    public void modify(long id, Team team)
    {
        Racer racer = racerRepository.find(id);
        if (racer == null)
            throw new NotFoundException(string.Format("Racer with id: {0} was not found", id));

        racer.team = team;
        racerRepository.modify(racer);
        
        Request req = new Request(RequestType.RACER_UPDATE, "{\"id\":" + racer.id + "}");
        notifier.NotifyPeer(req.ToString());
    }

    public void add(Observer o)
    {
        if(!observers.Contains(o))
            observers.Add(o);
    }

    public void remove(Observer o)
    {
        observers.Remove(o);
    }

    public void notify(string message)
    {
        observers.ForEach(o => o.update(message));
    }
}