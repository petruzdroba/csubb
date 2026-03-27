package org.zdroba.demo;

import org.zdroba.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DeadLockOrder {

    public static void runDemo() {

        try (Connection connA = DBConnection.getConnection();
             Connection connB = DBConnection.getConnection()) {

            System.out.println("Deadlock Order");

            connA.setAutoCommit(false);
            connB.setAutoCommit(false);
            System.out.println("Auto Commit set false for connections");

            Thread tA = new Thread(() -> {
                try {
                    System.out.println("Transaction A: BEGIN");

                    PreparedStatement ps = connA.prepareStatement("UPDATE employees SET salary = 6000 WHERE id = 5");
                    ps.executeUpdate();
                    System.out.println("Transaction A: UPDATE");

                    System.out.println("Transaction A: WAIT");
                    Thread.sleep(2000);

                    PreparedStatement ps2 = connA.prepareStatement("UPDATE employees SET salary = 7000 WHERE id = 6");
                    ps2.executeUpdate();

                    connA.commit();
                    System.out.println("Transaction A: UPDATE + COMMIT");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread tB = new Thread(() -> {
                try {
                    System.out.println("Transaction B: BEGIN");

                    PreparedStatement ps = connB.prepareStatement("UPDATE employees SET salary = 6000 WHERE id = 5");
                    ps.executeUpdate();
                    System.out.println("Transaction B: UPDATE");

                    System.out.println("Transaction B: WAIT");
                    Thread.sleep(2000);

                    PreparedStatement ps2 = connB.prepareStatement("UPDATE employees SET salary = 7000 WHERE id = 6");
                    ps2.executeUpdate();

                    connB.commit();
                    System.out.println("Transaction B: UPDATE + COMMIT");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            tA.start();
            tB.start();

            tA.join();
            tB.join();

            PreparedStatement psFinal = connA.prepareStatement(
                    "SELECT id, salary FROM employees WHERE id IN (5, 6) ORDER BY id"
            );
            ResultSet rs = psFinal.executeQuery();

            System.out.println("Final state in DB:");
            while (rs.next()) {
                int id = rs.getInt("id");
                int salary = rs.getInt("salary");
                System.out.println("Employee " + id + " salary = " + salary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
