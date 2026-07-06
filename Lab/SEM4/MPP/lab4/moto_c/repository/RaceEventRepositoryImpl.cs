using moto_c.entity;
using MySqlConnector;
using NLog;

namespace moto_c.repository;

public class RaceEventRepositoryImpl:RaceEventRepository
{
    private readonly MySqlConnection _connection = DatabaseConnection.getInstance().GetConnection();
    private static readonly Logger logger = LogManager.GetCurrentClassLogger();
    
    private static RaceEventRepositoryImpl? instance;

    private RaceEventRepositoryImpl() {}

    public static RaceEventRepositoryImpl getInstance()
    {
        if (instance == null)
            instance = new RaceEventRepositoryImpl();
        return instance;
    }
    
    public List<RaceEvent> getAll()
    {
        logger.Trace("Entry GetAll");
        string sql = "SELECT * FROM events";
        List<RaceEvent> events = new List<RaceEvent>();
        
        try
        {
            using (MySqlCommand cmd = new MySqlCommand(sql, _connection))
            {
                using (MySqlDataReader rs = cmd.ExecuteReader())
                {
                    while (rs.Read())
                    {
                        long id = rs.GetInt64("id");
                        int engine = rs.GetInt32("engine");
                        
                        RaceEvent raceEvent = new RaceEvent(engine);
                        raceEvent.id = id;
                        
                        events.Add(raceEvent);
                    }
                }
            }
        }
        catch (MySqlException e)
        {
            logger.Error(e.Message);
            Console.Error.WriteLine(e.Message);
        }
        
        logger.Trace("Exit GetAll");
        return events;
    }

    public void add(RaceEvent raceEvent)
    {
        logger.Trace("Entry Add with engine={}", raceEvent.engine);
        string sql = "INSERT INTO events (engine) VALUES (@engine)";

        try
        {
            using (MySqlCommand cmd = new MySqlCommand(sql, _connection))
            {
                cmd.Parameters.AddWithValue("@engine", raceEvent.engine);
                int result = cmd.ExecuteNonQuery();
                logger.Trace("Saved {} instance", result);
            }
        }
        catch (MySqlException e)
        {
            logger.Error(e.Message);
            Console.Error.WriteLine(e.Message);
        }
        
        logger.Trace("Exit Add");
    }

    public RaceEvent? find(int engine)
    {
        logger.Trace("Entry Find with engine={}", engine);
        string sql = "SELECT * FROM events WHERE engine=@engine";
        
        try
        {
            using (MySqlCommand cmd = new MySqlCommand(sql, _connection))
            {
                cmd.Parameters.AddWithValue("@engine", engine);

                using (MySqlDataReader rs = cmd.ExecuteReader())
                {
                    if (rs.Read())
                    {
                        long id = rs.GetInt64("id");
                        
                        RaceEvent raceEvent = new RaceEvent(engine);
                        raceEvent.id = id;
                        
                        logger.Trace("Exit Find with engine={}", engine);
                        return raceEvent;
                    }
                }
            }
        }
        catch (MySqlException e)
        {
            logger.Error(e.Message);
            Console.Error.WriteLine(e.Message);
        }
        
        logger.Trace("Exit Find with engine={}", engine);
        return null;
    }
}