package org.zdroba.demo;

import org.zdroba.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LostUpdate {

    public static void runDemo() {
        try (Connection connA = DBConnection.getConnection();
             Connection connB = DBConnection.getConnection()) {

            System.out.println("Lost Update");

            connA.setAutoCommit(false);
            connB.setAutoCommit(false);
            System.out.println("Auto Commit set false for connections");

            connA.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            System.out.println("Transaction A isolation set to SERIALIZABLE");

            Thread tA = new Thread(()->{
                try{
                    System.out.println("Transaction A: BEGIN");

                    PreparedStatement ps = connA.prepareStatement("SELECT salary FROM employees WHERE id = 4");
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        int salary = rs.getInt(1);
                        System.out.println("Transaction A: Base salary "+salary);
                    }

                    System.out.println("Transaction A: waiting on B");
                    Thread.sleep(5000);

                    PreparedStatement ps2 = connA.prepareStatement("UPDATE employees SET salary=6000 WHERE id=4");
                    ps2.executeUpdate();
                    connA.commit();

                    System.out.println("Transaction A: UPDATE salary to 6000 + COMMIT");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread tB = new Thread(()->{
                try{
                    System.out.println("Transaction B: BEGIN");

                    PreparedStatement ps = connB.prepareStatement("SELECT salary FROM employees WHERE id = 4");
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        int salary = rs.getInt(1);
                        System.out.println("Transaction B: Base salary "+salary);
                    }

                    PreparedStatement ps2 = connB.prepareStatement("UPDATE employees SET salary=5500 WHERE id=4");
                    ps2.executeUpdate();
                    connB.commit();

                    System.out.println("Transaction B: UPDATE salary to 5500 + COMMIT");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            tA.start();
            tB.start();

            tA.join();
            tB.join();

            PreparedStatement psFinal = connB.prepareStatement(
                    "SELECT salary FROM employees WHERE id = 4"
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
