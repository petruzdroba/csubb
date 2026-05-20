package org.zdroba.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdroba.entity.RaceEvent;

import java.util.List;

public class RaceEventRepositoryJPA implements RaceEventRepository {

    private static final Logger logger = LogManager.getLogger();

    private EntityManager em() {
        return JPAUtil.getEntityManager();
    }

    @Override
    public RaceEvent find(Long id) {
        logger.traceEntry();

        EntityManager em = em();
        try {
            RaceEvent event = em.find(RaceEvent.class, id);

            logger.traceExit(event);
            return event;
        } finally {
            em.close();
        }
    }

    @Override
    public RaceEvent find(int engine) {
        logger.traceEntry();

        EntityManager em = em();
        try {
            RaceEvent event = em.createQuery(
                            "SELECT e FROM RaceEvent e WHERE e.engine = :engine",
                            RaceEvent.class
                    )
                    .setParameter("engine", engine)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            logger.traceExit(event);
            return event;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(RaceEvent raceEvent) {
        logger.traceEntry("updating RaceEvent {}", raceEvent);

        EntityManager em = em();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            RaceEvent existing = em.find(RaceEvent.class, raceEvent.getId());
            if (existing == null) {
                throw new RuntimeException("RaceEvent not found with id " + raceEvent.getId());
            }

            existing.setEngine(raceEvent.getEngine());

            em.merge(existing);

            tx.commit();

            logger.trace("Updated {}", existing);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error(e.getMessage(), e);
        } finally {
            em.close();
        }

        logger.traceExit();
    }

    @Override
    public void delete(Long key) {
        logger.traceEntry("deleting RaceEvent with id {}", key);

        EntityManager em = em();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            RaceEvent entity = em.find(RaceEvent.class, key);
            if (entity == null) {
                throw new RuntimeException("RaceEvent not found with id " + key);
            }

            em.remove(entity);

            tx.commit();

            logger.trace("Deleted RaceEvent {}", key);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error(e.getMessage(), e);
        } finally {
            em.close();
        }

        logger.traceExit();
    }

    @Override
    public List<RaceEvent> getAll() {
        logger.traceEntry();

        EntityManager em = em();
        try {
            List<RaceEvent> events = em.createQuery(
                    "FROM RaceEvent",
                    RaceEvent.class
            ).getResultList();

            logger.traceExit(events);
            return events;
        } finally {
            em.close();
        }
    }

    @Override
    public void add(RaceEvent raceEvent) {
        logger.traceEntry("saving RaceEvent {}", raceEvent);

        EntityManager em = em();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(raceEvent);
            tx.commit();

            logger.trace("Saved {}", raceEvent);
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            logger.error(e.getMessage(), e);
        } finally {
            em.close();
        }

        logger.traceExit();
    }
}