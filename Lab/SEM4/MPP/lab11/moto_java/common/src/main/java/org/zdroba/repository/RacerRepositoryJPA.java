package org.zdroba.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdroba.entity.RaceEvent;
import org.zdroba.entity.Racer;
import org.zdroba.entity.Team;

import java.util.List;

public class RacerRepositoryJPA implements RacerRepository {

    private static final Logger logger = LogManager.getLogger();

    private EntityManager em() {
        return JPAUtil.getEntityManager();
    }

    @Override
    public Racer find(Long id) {
        logger.traceEntry();

        EntityManager em = em();
        try {
            Racer racer = em.find(Racer.class, id);

            logger.traceExit(racer);
            return racer;
        } finally {
            em.close();
        }
    }

    @Override
    public Racer find(String cnp) {
        logger.traceEntry();

        EntityManager em = em();
        try {
            Racer racer = em.createQuery(
                            "SELECT r FROM Racer r WHERE r.cnp = :cnp",
                            Racer.class
                    )
                    .setParameter("cnp", cnp)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            logger.traceExit(racer);
            return racer;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Racer> getAll() {
        logger.traceEntry();

        EntityManager em = em();
        try {
            List<Racer> racers = em.createQuery(
                    "FROM Racer",
                    Racer.class
            ).getResultList();

            logger.traceExit(racers);
            return racers;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Racer> getBy(Team team) {
        logger.traceEntry();

        EntityManager em = em();
        try {
            List<Racer> racers = em.createQuery(
                            "SELECT r FROM Racer r WHERE r.team = :team",
                            Racer.class
                    )
                    .setParameter("team", team)
                    .getResultList();

            logger.traceExit(racers);
            return racers;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Racer> getBy(RaceEvent engine) {
        logger.traceEntry();

        EntityManager em = em();
        try {
            List<Racer> racers = em.createQuery(
                            "SELECT r FROM Racer r WHERE r.engine.engine = :engine",
                            Racer.class
                    )
                    .setParameter("engine", engine.getEngine())
                    .getResultList();

            logger.traceExit(racers);
            return racers;
        } finally {
            em.close();
        }
    }

    @Override
    public void add(Racer racer) {
        logger.traceEntry("saving Racer {}", racer);

        EntityManager em = em();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(racer);
            tx.commit();

            logger.trace("Saved {}", racer);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error(e.getMessage(), e);
        } finally {
            em.close();
        }

        logger.traceExit();
    }

    @Override
    public void modify(Racer racer) {
        logger.traceEntry("Modifying Racer {}", racer.getId());

        EntityManager em = em();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(racer);
            tx.commit();

            logger.trace("Updated {}", racer);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error(e.getMessage(), e);
        } finally {
            em.close();
        }

        logger.traceExit();
    }
}