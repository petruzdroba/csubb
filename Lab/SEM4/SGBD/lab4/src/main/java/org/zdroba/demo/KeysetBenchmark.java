package org.zdroba.demo;

import jakarta.persistence.EntityManager;
import org.zdroba.JPAUtil;

import java.util.List;

public class KeysetBenchmark {

    private final static int pageSize = 50;

    public static void main(String args[]) {
        first(pageSize);
        middle(pageSize);
        last(pageSize);
    }

    static void first(int size) {
        EntityManager em = JPAUtil.getEntityManager();

        long start = System.currentTimeMillis();

        var res = em.createQuery(
                        "SELECT e FROM Employee e WHERE e.id > 0 ORDER BY e.id",
                        Object.class)
                .setMaxResults(size)
                .getResultList();

        long end = System.currentTimeMillis();
        System.out.println("KEYSET FIRST: " + (end - start) + " ms");

        em.close();
    }

    static void middle(int size) {
        EntityManager em = JPAUtil.getEntityManager();

        List<Integer> ids = em.createQuery(
                        "SELECT e.id FROM Employee e ORDER BY e.id",
                        Integer.class)
                .setFirstResult(50000)
                .setMaxResults(size)
                .getResultList();

        Integer cursor = ids.isEmpty() ? 0 : ids.get(0);

        long start = System.currentTimeMillis();

        em.createQuery(
                        "SELECT e FROM Employee e WHERE e.id > :id ORDER BY e.id",
                        Object.class)
                .setParameter("id", cursor)
                .setMaxResults(size)
                .getResultList();

        long end = System.currentTimeMillis();
        System.out.println("KEYSET MIDDLE: " + (end - start) + " ms");

        em.close();
    }

    static void last(int size) {
        EntityManager em = JPAUtil.getEntityManager();

        Integer maxId = em.createQuery("SELECT MAX(e.id) FROM Employee e", Integer.class)
                .getSingleResult();

        long start = System.currentTimeMillis();

        em.createQuery(
                        "SELECT e FROM Employee e WHERE e.id <= :id ORDER BY e.id DESC",
                        Object.class)
                .setParameter("id", maxId)
                .setMaxResults(size)
                .getResultList();

        long end = System.currentTimeMillis();
        System.out.println("KEYSET LAST: " + (end - start) + " ms");

        em.close();
    }
}