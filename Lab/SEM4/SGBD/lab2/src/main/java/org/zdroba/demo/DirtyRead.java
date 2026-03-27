package org.zdroba.demo;

import org.zdroba.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DirtyRead {

    public static void runDemo() {
        try (Connection connA = DBConnection.getConnection();
             Connection connB = DBConnection.getConnection()) {

            System.out.println("Dirty Read");

            connA.setAutoCommit(false);
            connB.setAutoCommit(false);
            System.out.println("Auto Commit set false for connections");

            connB.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            System.out.println("Read Uncommited set true for connections");

            Thread tA = new Thread(() -> {
                try {
                    System.out.println("Transaction A: BEGIN");
                    PreparedStatement ps = connA.prepareStatement(
                            "UPDATE employees SET salary = 10000 WHERE id = 1"
                    );
                    ps.executeUpdate();
                    System.out.println("Transaction A: Salary updated to 10000 (uncommitted)");

                    Thread.sleep(5000);

                    connA.rollback();
                    System.out.println("Transaction A: ROLLBACK executed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread tB = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("Transaction B: BEGIN (READ UNCOMMITTED)");

                    PreparedStatement ps = connB.prepareStatement(
                            "SELECT salary FROM employees WHERE id = 1"
                    );
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int salary = rs.getInt("salary");
                        System.out.println("Transaction B: Read salary = " + salary);
                    }

                    connB.commit();
                    System.out.println("Transaction B: COMMIT executed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            tA.start();
            tB.start();

            tA.join();
            tB.join();

            PreparedStatement psFinal = connA.prepareStatement(
                    "SELECT salary FROM employees WHERE id = 1"
            );
            ResultSet rsFinal = psFinal.executeQuery();

            if (rsFinal.next()) {
                int salary = rsFinal.getInt("salary");
                System.out.println("Final salary in DB: " + salary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
