using MySqlConnector;
using Microsoft.Extensions.Configuration;

public class DatabaseConnection
{
    private static DatabaseConnection? instance;
    private readonly MySqlConnection connection;

    private DatabaseConnection()
    {
        var config = new ConfigurationBuilder()
            .AddJsonFile("appsettings.json")
            .Build();

        string url = config["Database:Url"]!;
        string user = config["Database:Username"]!;
        string password = config["Database:Password"]!;

        string connectionString = $"{url}User={user};Password={password};";
        connection = new MySqlConnection(connectionString);
        connection.Open();
    }

    public static DatabaseConnection getInstance()
    {
        if (instance == null)
            instance = new DatabaseConnection();
        return instance;
    }

    public MySqlConnection GetConnection()
    {
        return connection;
    }
}