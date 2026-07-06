namespace moto_c.entity;

public class RaceEvent
{
    public long id { get; set; }
    public int engine { get; set; }

    public RaceEvent()
    {
    }

    public RaceEvent(int engine)
    {
        this.engine = engine;
    }
}