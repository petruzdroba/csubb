package org.zdroba.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zdroba.JPAUtil;
import org.zdroba.entity.Park;
import org.zdroba.entity.Tag;
import org.zdroba.entity.Trail;

import java.util.List;

public class TagRepository implements ITagRepository{
    private static TagRepository instance;

    private TagRepository() {}

    public static TagRepository getInstance(){
        if(instance == null)
            instance = new TagRepository();
        return instance;
    }

    @Override
    public void add(Tag entity) {
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
    public void update(Long key, Tag entity) {
        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();

            transaction.begin();

            em.merge(entity);

            transaction.commit();
        }
    }

    @Override
    public Tag find(Long key) {
        try(EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()){
            return em.find(Tag.class, key);
        }
    }

    @Override
    public List<Tag> getAll() {
        List<Tag> tags;

        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            tags = em.createQuery("FROM Tag", Tag.class).getResultList();
        }

        return tags;
    }

    @Override
    public List<Long> getKeys() {
        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery("SELECT p.id FROM Tag p", Long.class)
                    .getResultList();
        }
    }
}
