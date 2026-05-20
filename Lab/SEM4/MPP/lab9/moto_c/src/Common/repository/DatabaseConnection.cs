using MySqlConnector;
using Microsoft.Extensions.Configuration;

public class DatabaseConnection
{
    private static DatabaseConnection? instance;
    private readonly string connectionString;

    private DatabaseConnection()
    {
        var config = new ConfigurationBuilder()
            .AddJsonFile("appsettings.json")
            .Build();

        string url = config["Database:Url"]!;
        string user = config["Database:Username"]!;
        string password = config["Database:Password"]!;

        connectionString = $"{url}User={user};Password={password};";
    }

    public static DatabaseConnection getInstance()
    {
        if (instance == null)
            instance = new DatabaseConnection();
        return instance;
    }

    public MySqlConnection GetConnection()
    {
        var connection = new MySqlConnection(connectionString);
        connection.Open();
        return connection;
    }
}