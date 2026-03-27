package org.zdroba.batch;

import org.zdroba.db.DBConnection;
import org.zdroba.db.DBInitializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LotCommit {

    public static void run() {
        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO employees (name, department_id) VALUE (?, ?)")) {
                for(int i = 0; i < 5000; i++){
                    ps.setString(1, "Employee"+i);
                    ps.setInt(2, 1);

                    ps.executeUpdate();
                    if(i % 100 == 0)
                        connection.commit();
                }
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double average() {
        DBInitializer.resetDatabase();

        long start = System.nanoTime();
        run();
        long end = System.nanoTime();

        return (double) (end - start) / 1_000_000;
    }
}
