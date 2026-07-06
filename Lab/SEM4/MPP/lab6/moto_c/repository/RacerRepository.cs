using moto_c.entity;

namespace moto_c.repository;

public interface RacerRepository : Repository<long, Racer>
{

    List<Racer> findBy(Team team);

    List<Racer> findBy(RaceEvent engine);
    
    void add(Racer racer);
    
    void modify(Racer racer);
}