package org.zdroba.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Trail;

import java.util.List;

public class TrailRepository implements ITrailRepository{

    private static TrailRepository instance;

    private TrailRepository() {}

    public static TrailRepository getInstance(){
        if(instance == null)
            instance = new TrailRepository();
        return instance;
    }

    @Override
    public void add(Trail entity) {
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

            Trail entity = em.find(Trail.class, key);
            if (entity != null) em.remove(entity);
        }

        transaction.commit();
    }

    @Override
    public void update(Long key, Trail entity) {
        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            transaction.begin();

            em.merge(entity);

            transaction.commit();
        }
    }

    @Override
    public Trail find(Long key) {
        try(EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()){
            return em.find(Trail.class, key);
        }
    }

    @Override
    public List<Trail> getAll() {
        List<Trail> trails;

        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            trails = em.createQuery("FROM Trail", Trail.class).getResultList();
        }

        return trails;
    }

    @Override
    public List<Long> getKeys() {
        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery("SELECT p.id FROM Trail p", Long.class)
                    .getResultList();
        }
    }
}
