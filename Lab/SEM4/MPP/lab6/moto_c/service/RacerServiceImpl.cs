using moto_c.entity;
using moto_c.exceptions;
using moto_c.repository;

namespace moto_c.service;

public class RacerServiceImpl : RacerService
{
    private readonly RacerRepository racerRepository;
    private readonly RaceEventRepository raceEventRepository;

    public RacerServiceImpl(RacerRepository racerRepository, RaceEventRepository raceEventRepository)
    {
        this.racerRepository = racerRepository;
        this.raceEventRepository = raceEventRepository;
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
    }

    public void modify(long id, Team team)
    {
        Racer racer = racerRepository.find(id);
        if (racer == null)
            throw new NotFoundException(string.Format("Racer with id: {0} was not found", id));

        racer.team = team;
        racerRepository.modify(racer);
    }
}