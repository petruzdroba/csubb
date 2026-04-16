package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.zdroba.JPAUtil;
import org.zdroba.JPAUtilNoPool;

public class ConnectionBenchmarks {

    public static void run(){
        int iterations = 100;

        EntityManagerFactory noPoolEmf = JPAUtilNoPool.getEntityManagerFactory();
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            EntityManager em = noPoolEmf.createEntityManager();
            em.getTransaction().begin();
            em.getTransaction().rollback();
            em.close();
        }
        long noPoolTime = System.currentTimeMillis() - start;

        EntityManagerFactory pooledEmf = JPAUtil.getEntityManagerFactory();
        start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            EntityManager em = pooledEmf.createEntityManager();
            em.getTransaction().begin();
            em.getTransaction().rollback();
            em.close();
        }
        long pooledTime = System.currentTimeMillis() - start;

        System.out.println("Overhead Creare conexiune");
        System.out.printf("No pool total: %dms \n", noPoolTime);
        System.out.printf("Hikari  total: %dms", pooledTime);
    }

    public static void main(String[] args) {
        run();
    }
}
