package org.example.repo;

import org.example.domain.DataBaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConnection {

    protected final DataBaseConfig config;

    public DatabaseConnection(String url, String user, String password) {
        config = new DataBaseConfig(url, user, password);
    }

    public DatabaseConnection(DataBaseConfig config) {
        this.config = config;
    }

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e.getMessage());
        }
        return DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
    }
}
