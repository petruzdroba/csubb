package org.zdroba.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("default");

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }));
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}
