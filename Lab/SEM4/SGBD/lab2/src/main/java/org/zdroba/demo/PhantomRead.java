package org.zdroba.demo;

import org.zdroba.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PhantomRead {

    public static void runDemo() {
        try (Connection connA = DBConnection.getConnection();
             Connection connB = DBConnection.getConnection()) {

            System.out.println("Phantom Read");

            connA.setAutoCommit(false);
            connB.setAutoCommit(false);
            System.out.println("Auto Commit set false for connections");

            connA.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            Thread tA = new Thread(() -> {
                try {
                    System.out.println("Transaction A: BEGIN");

                    PreparedStatement ps = connA.prepareStatement("SELECT COUNT(*) FROM employees WHERE department_id = 5");
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        int count = rs.getInt(1);
                        System.out.println("Transaction A: First count "+count);
                    }

                    System.out.println("Transaction A: waiting on B");
                    Thread.sleep(5000);

                    PreparedStatement ps2 = connA.prepareStatement("SELECT COUNT(*) FROM employees WHERE department_id = 5");
                    ResultSet rs2 = ps2.executeQuery();
                    if(rs2.next()){
                        int count = rs2.getInt(1);
                        System.out.println("Transaction A: Second count "+count);
                    }

                    connA.commit();
                    System.out.println("Transaction A: COMMIT");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread tB = new Thread(() -> {
                try{
                    System.out.println("Transaction B: BEGIN");

                    PreparedStatement ps = connB.prepareStatement("INSERT INTO employees (name, department_id) VALUE  ('Angajat Nou', 5)");
                    ps.executeUpdate();

                    connB.commit();
                    System.out.println("Transaction B: INSERT and COMMIT");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            tA.start();;
            tB.start();

            tA.join();
            tB.join();

            PreparedStatement psFinal = connA.prepareStatement("SELECT COUNT(*) FROM employees WHERE department_id = 5");
            ResultSet rsFinal = psFinal.executeQuery();
            if(rsFinal.next()) {
                System.out.println("Final count in DB: " + rsFinal.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
