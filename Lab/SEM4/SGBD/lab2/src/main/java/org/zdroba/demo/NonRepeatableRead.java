package org.zdroba.demo;

import org.zdroba.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NonRepeatableRead {

    public static void runDemo(){
        try (Connection connA = DBConnection.getConnection();
             Connection connB = DBConnection.getConnection()) {

            System.out.println("Non-Repeatable Read");

            connA.setAutoCommit(false);
            connB.setAutoCommit(false);
            System.out.println("Auto Commit set false for connections");

            connA.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            Thread tA = new Thread(() -> {
                try {
                    System.out.println("Transaction A: BEGIN");
                    PreparedStatement ps = connA.prepareStatement("SELECT salary FROM employees where id=2");

                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        int salary = rs.getInt("salary");
                        System.out.println("Transaction A: Read salary = " + salary);
                    }

                    System.out.println("Transaction A: waiting for B to finish");
                    Thread.sleep(5000);

                    ResultSet rs2 = ps.executeQuery();
                    if(rs2.next()){
                        int salary = rs2.getInt("salary");
                        System.out.println("Transaction A: Second read salary = " + salary);
                    }

                    connA.commit();
                    System.out.println("Transaction A: COMMIT");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread tB = new Thread(()->{
                try{
                    System.out.println("Transaction B: BEGIN");

                    System.out.println("Transaction B: waiting for A to do first read");
                    Thread.sleep(1000);

                    PreparedStatement ps = connB.prepareStatement("UPDATE employees SET salary=12000 WHERE id=2");
                    ps.executeUpdate();
                    connB.commit();
                    System.out.println("Transaction B: Salary updated and committed");

                }catch(Exception e){
                    e.printStackTrace();
                }
            });

            tA.start();
            tB.start();

            tA.join();
            tB.join();

            PreparedStatement psFinal = connB.prepareStatement(
                    "SELECT salary FROM employees WHERE id = 2"
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
