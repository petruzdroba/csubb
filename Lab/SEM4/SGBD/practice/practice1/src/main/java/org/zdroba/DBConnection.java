package org.zdroba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/sgbd_lab2?serverTimezone=UTC";
    private static final String USER = "student";
    private static final String PASSWORD = "student";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}