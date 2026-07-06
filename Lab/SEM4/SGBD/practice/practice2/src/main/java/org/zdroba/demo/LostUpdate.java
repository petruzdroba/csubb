package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import org.zdroba.DBConnection;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Parks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LostUpdate {

    public static void main(String[] args) {
        try (Connection conA = DBConnection.getConnection();
             Connection conB = DBConnection.getConnection()) {

            conA.setAutoCommit(false);
            conB.setAutoCommit(false);

            conA.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

            Thread tA = new Thread(() -> {
                try {

                    System.out.println("Trans A: begin");
                    PreparedStatement ps = conA.prepareStatement("SELECT size FROM parks WHERE id = 2");

                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        int size = rs.getInt("size");
                        System.out.println("Trans A: base size "+size);
                    }

                    System.out.println("Trans A: waiting on Trans B");
                    Thread.sleep(5000);

                    PreparedStatement ps2 = conA.prepareStatement("UPDATE parks SET size=2345 WHERE id=2");
                    ps2.executeUpdate();
                    conA.commit();

                    System.out.println("Trans A: updated value to 2345 + commit");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Thread tB = new Thread(()->{
                try{
                    System.out.println("Trans B: begin");
                    PreparedStatement ps = conB.prepareStatement("SELECT size FROM parks WHERE id =2");

                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        int size = rs.getInt("size");
                        System.out.println("Trans B: base size "+size);
                    }

                    PreparedStatement ps2=conB.prepareStatement("UPDATE parks SET size=3456 WHERE id=2");
                    ps2.executeUpdate();
                    conB.commit();

                    System.out.println("Trans B: updated value to 3456 + commit");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            tA.start();
            tB.start();

            tA.join();
            tB.join();

            PreparedStatement psFinal = conB.prepareStatement("SELECT size FROM parks WHERE id=2");
            ResultSet rsFinal = psFinal.executeQuery();
            if(rsFinal.next()){
                System.out.println("Final size in DB= "+rsFinal.getInt("size"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            fix();
        }
    }

    private static void fix(){
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        Parks park = em.find(Parks.class, 2);
        park.setSize(4931000L);

        em.getTransaction().commit();
        em.close();
    }
}
