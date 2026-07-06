namespace moto_c.entity;

public class Racer
{
    public long id { get; set; }
    public string name { get; set; }
    public string cnp  { get; set; }
    public Team team { get; set; }
    public RaceEvent engine { get; set; }

    public Racer(string name, string cnp, RaceEvent engine)
    {
        this.name = name;
        this.cnp = cnp;
        this.engine = engine;
        this.team = Team.NONE;
    }
    
    public Racer()
    {}

    public Racer(string name, string cnp, Team team, RaceEvent engine)
    {
        this.name = name;
        this.cnp = cnp;
        this.team = team;
        this.engine = engine;
    }
}