package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.zdroba.JPAUtil;


public class PreparedStatementCaching {
    public static void main(String[] args) {
//        noReuse();
        reuse();
    }

    static void noReuse(){//2073 ms
        EntityManager em = JPAUtil.getEntityManager();

        long start = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.id = :id");
            query.setParameter("id", (long) i);
            query.getSingleResult();
        }
        long end = System.currentTimeMillis();
        System.out.println((end-start)+ " ms");

        em.close();
    }

    static void reuse(){//1970 ms
        EntityManager em = JPAUtil.getEntityManager();

        long start = System.currentTimeMillis();
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.id = :id");
        for (int i = 1; i <= 1000; i++) {
            query.setParameter("id", (long) i);
            query.getSingleResult();
        }
        long end = System.currentTimeMillis();
        System.out.println((end-start)+ " ms");

        em.close();
    }
}
