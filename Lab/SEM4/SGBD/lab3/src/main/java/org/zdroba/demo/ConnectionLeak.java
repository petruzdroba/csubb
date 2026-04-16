package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.zdroba.JPAUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConnectionLeak {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Detectare Scurgerilor de Conexiuni");

        demonstrateLeak();
        demonstrateFix();
    }

    static void demonstrateLeak() {
        System.out.println("Leak - nu inchidem conexiunile");
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        List<EntityManager> leaked = new ArrayList<>();

        try {
            for (int i = 1; i <= 11; i++) {
                System.out.println("Candidate nr " + i + " starts request");

                EntityManager em = emf.createEntityManager();
                em.getTransaction().begin();
                leaked.add(em);

                System.out.println("Connection opened no. " + i);
            }
        } catch (Exception e) {
            System.out.println("No Connections left");
            System.out.println("Candidate nr 11 exhausted");
            System.out.println("Cause: " + e.getMessage());
        } finally {
            System.out.println("\nClean up");
            for (EntityManager em : leaked) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }
    }

    static void demonstrateFix() throws InterruptedException {
        System.out.println("Fix - inchidem conexiunile");

        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();

        ExecutorService executor = Executors.newFixedThreadPool(20);

        for (int i = 1; i <= 20; i++) {
            final int num = i;
            executor.submit(() -> {
                try (EntityManager em = emf.createEntityManager()) {
                    em.getTransaction().begin();
                    em.getTransaction().rollback();
                    System.out.printf("Connection #%d used and returned to pool OK%n", num);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.MINUTES);

        System.out.println("Done.");
    }
}
