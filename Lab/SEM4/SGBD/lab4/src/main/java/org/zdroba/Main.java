package org.zdroba;

import jakarta.persistence.EntityManager;

public class Main {
    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();
        System.out.println("Connection works: " + em.isOpen());
        em.close();
        JPAUtil.close();
    }
}