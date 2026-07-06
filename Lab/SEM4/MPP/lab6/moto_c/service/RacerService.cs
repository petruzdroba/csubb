using moto_c.entity;

namespace moto_c.service;

public interface RacerService
{
    List<Racer> find(Team team);

    List<Racer> getAll();

    List<Racer> getAll(int engine);

    void add(string nume, string cnp, int engine, Team team);
    
    void modify(long id, Team team);
}