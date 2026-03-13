package org.zdroba.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance = null;

    private final String url = "jdbc:postgresql://localhost:5432/sgbd";
    private final String user = "sn_user";
    private final String password = "sn_pass";
    private static Connection connection = null;

    private DatabaseConnection() {}

    public static DatabaseConnection getInstance(){
        if(instance == null)
            instance = new DatabaseConnection();
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed())
                connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
