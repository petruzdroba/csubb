package org.zdroba.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnection {

    private static DataBaseConnection instance;
    private final Connection connection;

    private DataBaseConnection() {
        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.config")) {

            if (input == null)
                throw new RuntimeException("db.config file not found");

            props.load(input);
            String url = props.getProperty("jdbc.url");
            String user = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");

            this.connection = DriverManager.getConnection(url, user, password);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public static DataBaseConnection getInstance() {
        if (instance == null)
            instance = new DataBaseConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}