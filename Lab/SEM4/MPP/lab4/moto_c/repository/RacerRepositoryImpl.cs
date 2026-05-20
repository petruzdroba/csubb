using moto_c.entity;
using MySqlConnector;
using NLog;

namespace moto_c.repository;

public class RacerRepositoryImpl:RacerRepository
{
    private readonly MySqlConnection _connection = DatabaseConnection.getInstance().GetConnection();
    private static readonly Logger logger = LogManager.GetCurrentClassLogger();
    private readonly RaceEventRepository _raceEventRepository = RaceEventRepositoryImpl.getInstance();
    private static RacerRepositoryImpl? instance;

    private RacerRepositoryImpl() {}

    public static RacerRepositoryImpl getInstance()
    {
        if (instance == null)
            instance = new RacerRepositoryImpl();
        return instance;
    }
    
    public Racer? find(long id)
    {
        logger.Trace("Entry Find id={}", id);
        string sql = "SELECT * FROM racers WHERE id = ? LIMIT 1";

        try
        {
            using (MySqlCommand cmd = new MySqlCommand(sql, _connection))
            {
                cmd.Parameters.AddWithValue("@id", id);

                using (MySqlDataReader rs = cmd.ExecuteReader())
                {
                    if (rs.Read())
                    {
                        string name = rs.GetString("name");
                        string cnp = rs.GetString("cnp");
                        RaceEvent? engine = _raceEventRepository.find(rs.GetInt32("engine"));
                        Team team = Enum.Parse<Team>(rs.GetString("team"));

                        Racer racer = new Racer(name, cnp, team, engine!);
                        racer.id = id;

                        logger.Trace("Exit Find id={}", id);
                        return racer;
                    }
                }
            }
        }
        catch (MySqlException e)
        {
            logger.Error(e.Message);
            Console.Error.WriteLine(e.Message);
        }

        logger.Trace("Exit Find id={}", id);
        return null;
    }

    public List<Racer> getAll()
    {
        logger.Trace("Entry GetAll");
        string sql = "SELECT * FROM racers";
        List<Racer> racers = new List<Racer>();

        try
        {
            using (MySqlCommand command = new MySqlCommand(sql, _connection))
            {
                using (MySqlDataReader rs = command.ExecuteReader())
                {
                    while (rs.Read())
                    {
                        long id = rs.GetInt64("id");
                        string name = rs.GetString("name");
                        string cnp = rs.GetString("cnp");
                        RaceEvent? engine = _raceEventRepository.find(rs.GetInt32("engine"));
                        Team team = Enum.Parse<Team>(rs.GetString("team"));

                        Racer racer = new Racer(name, cnp, team, engine!);
                        racer.id = id;
                        
                        racers.Add(racer);
                    }
                }
            }
        }
        catch (MySqlException ex)
        {
            logger.Error(ex.Message);
            Console.Error.WriteLine(ex.Message);
        }

        logger.Trace("Exit GetAll");
        return racers;
    }

    public List<Racer> findBy(Team team)
    {
        logger.Trace("Entry FindBy team={}", team);
        string sql = "SELECT * FROM racers WHERE team = (@team)";
        List<Racer> racers = new List<Racer>();

        try
        {
            using (MySqlCommand command = new MySqlCommand(sql, _connection))
            {
                command.Parameters.AddWithValue("@team", team.ToString());
                using (MySqlDataReader rs = command.ExecuteReader())
                {
                    while (rs.Read())
                    {
                        long id = rs.GetInt64("id");
                        string name = rs.GetString("name");
                        string cnp = rs.GetString("cnp");
                        RaceEvent? engine = _raceEventRepository.find(rs.GetInt32("engine"));

                        Racer racer = new Racer(name, cnp, team, engine!);
                        racer.id = id;
                        
                        racers.Add(racer);
                    }
                }
            }
        }
        catch (MySqlException ex)
        {
            logger.Error(ex.Message);
            Console.Error.WriteLine(ex.Message);
        }

        logger.Trace("Exit FindBy team={}", team);
        return racers;
    }

    public List<Racer> findBy(RaceEvent engine)
    {
        logger.Trace("Entry FindBy engine={}", engine.engine);
        string sql = "SELECT * FROM racers WHERE engine=(@engine)";
        List<Racer> racers = new List<Racer>();

        try
        {
            using (MySqlCommand command = new MySqlCommand(sql, _connection))
            {
                command.Parameters.AddWithValue("@engine", engine.engine);
                using (MySqlDataReader rs = command.ExecuteReader())
                {
                    while (rs.Read())
                    {
                        long id = rs.GetInt64("id");
                        string name = rs.GetString("name");
                        string cnp = rs.GetString("cnp");
                        Team team = Enum.Parse<Team>(rs.GetString("team"));

                        Racer racer = new Racer(name, cnp, team, engine);
                        racer.id = id;
                        
                        racers.Add(racer);
                    }
                }
            }
        }
        catch (MySqlException ex)
        {
            logger.Error(ex.Message);
            Console.Error.WriteLine(ex.Message);
        }

        logger.Trace("Exit FindBy engine={}", engine.engine);
        return racers;
    }

    public void add(Racer racer)
    {
        logger.Trace("Entry Add racer={}", racer.name);
        string sql = "INSERT INTO racers (name,cnp,engine, team) VALUES (@name,@cnp,@engine,@team)";

        try
        {
            using (MySqlCommand command = new MySqlCommand(sql, _connection))
            {
                command.Parameters.AddWithValue("@name", racer.name);
                command.Parameters.AddWithValue("@cnp", racer.cnp);
                command.Parameters.AddWithValue("@engine", racer.engine.engine);
                command.Parameters.AddWithValue("@team", racer.team.ToString());
                
                int result = command.ExecuteNonQuery();
                logger.Trace("Saved {} instance", result);
            }
        }
        catch (MySqlException ex)
        {
            logger.Error(ex.Message);
            Console.Error.WriteLine(ex.Message);
        }
        
        logger.Trace("Exit Add");
    }

    public void modify(Racer racer)
    {
        logger.Trace("Entry Modify racer id={}", racer.id);
        string sql = "UPDATE racers SET name=@name, cnp=@cnp, engine=@engine, team=@team WHERE id=@id";

        try
        {
            using (MySqlCommand command = new MySqlCommand(sql, _connection))
            {
                command.Parameters.AddWithValue("@name", racer.name);
                command.Parameters.AddWithValue("@cnp", racer.cnp);
                command.Parameters.AddWithValue("@engine", racer.engine.engine);
                command.Parameters.AddWithValue("@team", racer.team.ToString());
                command.Parameters.AddWithValue("@id", racer.id);
                
                command.ExecuteNonQuery();
            }
        }
        catch (MySqlException ex)
        {
            logger.Error(ex.Message);
            Console.Error.WriteLine(ex.Message);
        }
        
        logger.Trace("Exit Modify");
    }
}