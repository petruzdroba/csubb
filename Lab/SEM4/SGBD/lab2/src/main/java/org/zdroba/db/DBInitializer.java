package org.zdroba.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class DBInitializer {

    private static final String URL = "jdbc:mysql://localhost:3306/sgbd_lab2";
    private static final String USER = "student";
    private static final String PASSWORD = "student";

    public static void resetDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to database");

            executeSQLFile(conn, "src/main/resources/cleanup.sql");
            executeSQLFile(conn, "src/main/resources/schema.sql");
            executeSQLFile(conn, "src/main/resources/start.sql");

            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executeSQLFile(Connection conn, String path) throws IOException {
        String sql = new String(Files.readAllBytes(Paths.get(path)));

        String[] statements = sql.split(";");

        try (Statement stmt = conn.createStatement()) {
            for (String statement : statements) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        } catch (Exception e) {
            System.err.println("Error executing file: " + path);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        resetDatabase();
    }
}
