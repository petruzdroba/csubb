package org.zdroba.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance = null;

    private final String url = "jdbc:postgresql://localhost:5432/mydb";
    private final String user = "postgres";
    private final String password = "password";

    private DatabaseConnection() {}

    public DatabaseConnection getInstance(){
        if(instance == null)
            instance = new DatabaseConnection();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
