package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import org.zdroba.JPAUtil;
import org.zdroba.DBConnection;
import org.zdroba.entity.Parks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DirtyRead {

    public static void main(String[] args) {
        try(Connection conA = DBConnection.getConnection();
        Connection conB = DBConnection.getConnection()) {

            conA.setAutoCommit(false);
            conB.setAutoCommit(false);

            conB.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

            Thread t1 = new Thread(()->{
                System.out.println("T1: BEGIN");

                try{
                    PreparedStatement ps = conA.prepareStatement("UPDATE parks SET size=50000 WHERE id=1");
                    ps.executeUpdate();
                    System.out.println("T1: update size to 50000");

                    Thread.sleep(5000);


                    conA.rollback();
                    System.out.println("T1: rollback");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread t2 = new Thread(()->{
                try{
                    PreparedStatement psCheck = conB.prepareStatement("SELECT size FROM parks WHERE id=1");
                    ResultSet rsCheck = psCheck.executeQuery();
                    if(rsCheck.next()) {
                        System.out.println("T2 : initial size before T1 update = " + rsCheck.getInt("size"));
                    }

                    Thread.sleep(1000);
                    PreparedStatement ps = conB.prepareStatement("SELECT size FROM parks WHERE id=1");
                    ResultSet rs = ps.executeQuery();

                    if(rs.next()){
                        System.out.println("T2 : read size "+rs.getInt("size"));
                    }

                    conB.commit();
                    System.out.println("T2: commit executed");
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
        } finally {
            fix1();
        }
    }

    private static void fix1(){
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        Parks park = em.find(Parks.class, 1L);
        park.setSize(500L);
        em.merge(park);
        em.close();
    }
}
