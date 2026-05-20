package org.zdroba.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.zdroba.entity.RaceEvent;

import java.util.List;

@Repository
@Transactional
public class RaceEventRepositoryJPA implements RaceEventRepository {

    private static final Logger logger = LogManager.getLogger();

    @PersistenceContext
    private EntityManager em;

    @Override
    public RaceEvent find(Long id) {
        logger.traceEntry();
        RaceEvent event = em.find(RaceEvent.class, id);
        logger.traceExit(event);
        return event;
    }

    @Override
    public RaceEvent find(int engine) {
        logger.traceEntry();
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
    }

    @Override
    public void update(RaceEvent raceEvent) {
        logger.traceEntry("updating RaceEvent {}", raceEvent);
        RaceEvent existing = em.find(RaceEvent.class, raceEvent.getId());
        if (existing == null) {
            throw new RuntimeException("RaceEvent not found with id " + raceEvent.getId());
        }
        existing.setEngine(raceEvent.getEngine());
        em.merge(existing);
        logger.traceExit();
    }

    @Override
    public void delete(Long key) {
        logger.traceEntry("deleting RaceEvent with id {}", key);
        RaceEvent entity = em.find(RaceEvent.class, key);
        if (entity == null) {
            throw new RuntimeException("RaceEvent not found with id " + key);
        }
        em.remove(entity);
        logger.traceExit();
    }

    @Override
    public List<RaceEvent> getAll() {
        logger.traceEntry();
        List<RaceEvent> events = em.createQuery("FROM RaceEvent", RaceEvent.class)
                .getResultList();
        logger.traceExit(events);
        return events;
    }

    @Override
    public void add(RaceEvent raceEvent) {
        logger.traceEntry("saving RaceEvent {}", raceEvent);
        em.persist(raceEvent);
        logger.traceExit();
    }
}