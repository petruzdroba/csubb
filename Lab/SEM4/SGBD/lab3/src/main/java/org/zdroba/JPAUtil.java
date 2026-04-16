package org.zdroba;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("default");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
