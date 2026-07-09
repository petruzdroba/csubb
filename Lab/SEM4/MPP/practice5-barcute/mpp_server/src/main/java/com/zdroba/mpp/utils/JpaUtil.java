package com.zdroba.mpp.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

@Component
public class JpaUtil {
    private final EntityManagerFactory emf;

    public JpaUtil(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager em() {
        return emf.createEntityManager();
    }
}