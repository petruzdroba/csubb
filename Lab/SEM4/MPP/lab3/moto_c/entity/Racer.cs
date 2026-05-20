namespace moto_c.entity;

public class Racer
{
    private long id { get; set; }
    private string name { get; set; }
    private string cnp  { get; set; }
    private Team team { get; set; }
    private RaceEvent engine { get; set; }

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