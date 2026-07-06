package org.zdroba.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.zdroba.entity.User;

import java.util.List;

@Repository
@Transactional
public class UserRepositoryJPA implements UserRepository {

    private static final Logger logger = LogManager.getLogger();

    private EntityManager em() {
        return JPAUtil.getEntityManager();
    }

    @Override
    public User find(Long id) {
        logger.traceEntry();

        EntityManager em = em();
        try {
            User user = em.find(User.class, id);

            logger.traceExit();
            return user;
        } finally {
            em.close();
        }
    }

    @Override
    public User find(String email) {
        logger.traceEntry();

        EntityManager em = em();
        try {
            User user = em.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email",
                            User.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            logger.traceExit();
            return user;
        } finally {
            em.close();
        }
    }

    @Override
    public List<User> getAll() {
        logger.traceEntry();

        EntityManager em = em();
        try {
            List<User> users = em.createQuery(
                    "SELECT u FROM User u",
                    User.class
            ).getResultList();

            logger.traceExit();
            return users;
        } finally {
            em.close();
        }
    }

    @Override
    public void add(User user) {
        logger.traceEntry("Saving User {}", user);

        EntityManager em = em();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            em.persist(user);

            tx.commit();

            logger.trace("Saved entity {}", user);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error(e.getMessage(), e);
        } finally {
            em.close();
        }

        logger.traceExit();
    }
}