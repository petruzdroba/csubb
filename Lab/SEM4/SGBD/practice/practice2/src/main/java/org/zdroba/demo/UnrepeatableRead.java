package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import org.zdroba.DBConnection;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Parks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UnrepeatableRead {

    public static void main(String[] args) {
        try(Connection conA = DBConnection.getConnection();
        Connection conB = DBConnection.getConnection()){

            conA.setAutoCommit(false);
            conB.setAutoCommit(false);

            conA.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            Thread tA = new Thread(() ->{
                try{
                    System.out.println("Trans A begin");
                    PreparedStatement ps = conA.prepareStatement("SELECT size FROM parks WHERE id = 1");

                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        System.out.println("Trans A: Read size = "+rs.getInt("size"));
                    }

                    System.out.println("Trans A: waiting on Trans B");
                    Thread.sleep(5000);

                    ResultSet rs2 = ps.executeQuery();
                    if(rs2.next()){
                        System.out.println("Trans A: Read 2nd size = "+rs2.getInt("size"));
                    }

                    conA.commit();
                    System.out.println("Trans A: commit");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Thread tB = new Thread(() -> {
               try{
                   System.out.println("Trans B begin");

                   System.out.println("Trans B: waiting for A to read");
                   Thread.sleep(1000);

                   PreparedStatement ps = conB.prepareStatement("UPDATE parks SET size=1234 WHERE id = 1");
                   ps.executeUpdate();
                   conB.commit();
                   System.out.println("Trans B: updated size and commit");
               } catch (Exception e) {
                   throw new RuntimeException(e);
               }
            });

            tA.start();
            tB.start();

            tA.join();
            tB.join();

            PreparedStatement psFinal = conB.prepareStatement("SELECT size FROM parks WHERE id=1");
            ResultSet rsFinal = psFinal.executeQuery();
            if(rsFinal.next()){
                System.out.println("Final size in DB= "+rsFinal.getInt("size"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            fix();
        }
    }

    private static void fix(){
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        Parks park = em.find(Parks.class, 1);
        park.setSize(6641000L);

        em.getTransaction().commit();
        em.close();
    }

}
