package org.zdroba.demo;

import org.zdroba.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PhantomRead {

    public static void main(String[] args) {
        try(Connection conA = DBConnection.getConnection();
        Connection conB = DBConnection.getConnection()) {

            conA.setAutoCommit(false);
            conB.setAutoCommit(false);

            conA.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            Thread t1 = new Thread(()->{

                try {
                    PreparedStatement ps = conA.prepareStatement("SELECT COUNT(*) FROM parks WHERE size < 600");
                    ResultSet rs = ps.executeQuery();

                    if(rs.next()){
                        System.out.println("T1 selected "+rs.getInt(1));
                    }

                    System.out.println("T1: waiting on T2");
                    Thread.sleep(5000);

                    PreparedStatement ps2 = conA.prepareStatement("SELECT COUNT(*) FROM parks WHERE size < 600");
                    ResultSet rs2 = ps2.executeQuery();

                    if(rs2.next()){
                        System.out.println("T1 selected, second time "+rs2.getInt(1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            Thread t2 = new Thread(() -> {

                try {
                    Thread.sleep(1000);

                    PreparedStatement ps = conB.prepareStatement(
                            "INSERT INTO parks(size, name, country) VALUES (500, 'test', 'test')"
                    );
                    ps.executeUpdate();
                    System.out.println("T2: Inserted new park");

                    conB.commit();
                    System.out.println("T2: Commited");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            t1.start();
            t2.start();

            t1.join();
            t2.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
