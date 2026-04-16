package org.zdroba.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zdroba.entity.Park;

import java.util.List;

public class ParkRepository implements IParkRepository{

    private static ParkRepository instance;
    private ParkRepository() {}

    public static ParkRepository getInstance(){
        if(instance == null)
            instance = new ParkRepository();
        return instance;
    }

    @Override
    public void add(Park entity) {
        Transaction transaction;

        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            session.persist(entity);

            transaction.commit();
        }
    }

    @Override
    public void delete(Long key) {
        Transaction transaction;

        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            Park entity = session.get(Park.class, key);
            if(entity != null) session.remove(entity);

            transaction.commit();
        }
    }

    @Override
    public void update(Long key, Park entity) {
        Transaction transaction;

        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();
        }
    }

    @Override
    public Park find(Long key) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.createQuery("FROM Park p WHERE p.id = :key", Park.class)
                    .setParameter("key", key).getSingleResult();
        }
    }

    @Override
    public List<Park> getAll() {
        List<Park> parks;

        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            parks = session.createQuery("FROM Park", Park.class).list();
        }

        return parks;
    }

    @Override
    public List<Long> getKeys() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            return session.createQuery("SELECT p.id FROM Park p", Long.class)
                    .list();
        }
    }
}
