package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import org.zdroba.JPAUtil;

public class OffsetBenchmark {

    private final static int pageSize = 50;

    public static void main(String args[]) {
        first(pageSize);
        middle(pageSize);
        last(pageSize);
    }

    static void first(int size) {
        EntityManager em = JPAUtil.getEntityManager();

        long start = System.currentTimeMillis();

        em.createQuery("SELECT e FROM Employee e ORDER BY e.id", Object.class)
                .setFirstResult(0)
                .setMaxResults(size)
                .getResultList();

        long end = System.currentTimeMillis();
        System.out.println("OFFSET FIRST: " + (end - start) + " ms");

        em.close();
    }

    static void middle(int size) {
        EntityManager em = JPAUtil.getEntityManager();

        int offset = 50 * size;

        long start = System.currentTimeMillis();

        em.createQuery("SELECT e FROM Employee e ORDER BY e.id", Object.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();

        long end = System.currentTimeMillis();
        System.out.println("OFFSET MIDDLE: " + (end - start) + " ms");

        em.close();
    }

    static void last(int size) {
        EntityManager em = JPAUtil.getEntityManager();

        Long total = em.createQuery("SELECT COUNT(e) FROM Employee e", Long.class)
                .getSingleResult();

        int offset = (int)((total - 1) / size) * size;

        long start = System.currentTimeMillis();

        em.createQuery("SELECT e FROM Employee e ORDER BY e.id", Object.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();

        long end = System.currentTimeMillis();
        System.out.println("OFFSET LAST: " + (end - start) + " ms");

        em.close();
    }
}