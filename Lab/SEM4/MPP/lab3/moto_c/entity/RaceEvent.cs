namespace moto_c.entity;

public class RaceEvent
{
    private long id { get; set; }
    private int engine { get; set; }

    public RaceEvent()
    {
    }

    public RaceEvent(int engine)
    {
        this.engine = engine;
    }
}