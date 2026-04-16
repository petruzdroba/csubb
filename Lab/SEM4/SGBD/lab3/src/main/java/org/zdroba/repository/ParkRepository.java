package org.zdroba.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Park;

import java.util.List;

public class ParkRepository implements IParkRepository {

    private static ParkRepository instance;

    private ParkRepository() {
    }

    public static ParkRepository getInstance() {
        if (instance == null)
            instance = new ParkRepository();
        return instance;
    }

    @Override
    public void add(Park entity) {
        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            transaction.begin();

            em.persist(entity);

            transaction.commit();
        }
    }

    @Override
    public void delete(Long key) {
        EntityTransaction transaction;
        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            transaction = em.getTransaction();

            transaction.begin();

            Park entity = em.find(Park.class, key);
            if (entity != null) em.remove(entity);
        }

        transaction.commit();
    }

    @Override
    public void update(Long key, Park entity) {
        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            transaction.begin();

            em.merge(entity);

            transaction.commit();
        }
    }

    @Override
    public Park find(Long key) {
        try(EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()){
            return em.find(Park.class, key);
        }
    }

    @Override
    public List<Park> getAll() {
        List<Park> parks;

        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            parks = em.createQuery("FROM Park", Park.class).getResultList();
        }

        return parks;
    }

    @Override
    public List<Long> getKeys() {
        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery("SELECT p.id FROM Park p", Long.class)
                    .getResultList();
        }
    }
}
