package org.zdroba;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtilNoPool {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("no-pool");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
