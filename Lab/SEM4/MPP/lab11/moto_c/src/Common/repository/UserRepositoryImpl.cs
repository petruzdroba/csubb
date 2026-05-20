using moto_c.entity;
using MySqlConnector;
using NLog;

namespace moto_c.repository;

public class UserRepositoryImpl: UserRepository
{

    private readonly MySqlConnection _connection = DatabaseConnection.getInstance().GetConnection();
    private static readonly Logger logger = LogManager.GetCurrentClassLogger();
    
    public User? find(long id)
    {
        logger.Trace("Entry Find with id={}", id);
        string sql = "SELECT * FROM users WHERE id = ? LIMIT 1";

        try
        {
            using (MySqlCommand cmd = new MySqlCommand(sql, _connection))
            {
                cmd.Parameters.AddWithValue("@id", id);

                using (MySqlDataReader rs = cmd.ExecuteReader())
                {
                    if (rs.Read())
                    {
                        string email = rs.GetString("email");
                        string password = rs.GetString("password");

                        User user = new User(email, password);
                        user.id = id;
                        
                        logger.Trace("Exit Find with id={}", id);
                        return user;
                    }
                }
            }
        }
        catch (MySqlException e)
        {
            logger.Error(e.Message);
            Console.Error.WriteLine(e.Message);
        }

        logger.Trace("Exit Find with id={}", id);
        return null;
    }

    public User? find(string email)
    {
        logger.Trace("Entry Find with email={}", email);
        string sql = "SELECT * FROM users WHERE email = ? LIMIT 1";

        try
        {
            using (MySqlCommand cmd = new MySqlCommand(sql, _connection))
            {
                cmd.Parameters.AddWithValue("@email", email);

                using (MySqlDataReader rs = cmd.ExecuteReader())
                {
                    if (rs.Read())
                    {
                        long id = rs.GetInt64("id");
                        string password = rs.GetString("password");

                        User user = new User(email, password);
                        user.id = id;
                        
                        logger.Trace("Exit Find with email={}", email);
                        return user;
                    }
                }
            }
        }
        catch (MySqlException e)
        {
            logger.Error(e.Message);
            Console.Error.WriteLine(e.Message);
        }

        logger.Trace("Exit Find with email={}", email);
        return null;
    }

    public List<User> getAll()
    {
        logger.Trace("Entry GetAll");
        string sql = "SELECT * FROM users";
        List<User> users = new List<User>();
        
        try
        {
            using (MySqlCommand cmd = new MySqlCommand(sql, _connection))
            {
                using (MySqlDataReader rs = cmd.ExecuteReader())
                {
                    while (rs.Read())
                    {
                        long id = rs.GetInt64("id");
                        string email = rs.GetString("email");
                        string password = rs.GetString("password");

                        User user = new User(email, password);
                        user.id = id;
                        users.Add(user);
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
        return users;
    }

    public void add(User user)
    {
        logger.Trace("Entry Add with id={}", user.id);
        string sql = "INSERT INTO users (email, password) VALUES (@email, @password)";

        try
        {
            using (MySqlCommand cmd = new MySqlCommand(sql, _connection))
            {
                cmd.Parameters.AddWithValue("@email", user.email);
                cmd.Parameters.AddWithValue("@password", user.password);

                int result = cmd.ExecuteNonQuery();

                user.id = cmd.LastInsertedId;
                logger.Trace("Saved {} instance", result);
            }
        }
        catch (MySqlException e)
        {
            logger.Error(e.Message);
            Console.Error.WriteLine(e.Message);
        }
        
        logger.Trace("Exit Add with id={}", user.id);
    }
}