package com.zdroba.mpp.repository;

import com.zdroba.mpp.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericRepo<T, ID> {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    private final Class<T> entityClass;
    final JpaUtil jpaUtil;

    public GenericRepo(Class<T> entityClass, JpaUtil jpaUtil) {
        this.entityClass = entityClass;
        this.jpaUtil = jpaUtil;
        log.info("GenericRepo ready for: {}", entityClass.getSimpleName());
    }

    public Optional<T> findById(ID id) {
        log.info("[{}] findById: {}", entityClass.getSimpleName(), id);
        try (EntityManager em = jpaUtil.em()) {
            Optional<T> result = Optional.ofNullable(em.find(entityClass, id));
            log.info("[{}] findById {} -> {}", entityClass.getSimpleName(), id, result.isPresent() ? "found" : "not found");
            return result;
        }
    }

    public List<T> findAll() {
        log.info("[{}] findAll", entityClass.getSimpleName());
        try (EntityManager em = jpaUtil.em()) {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            List<T> results = em.createQuery(jpql, entityClass).getResultList();
            log.info("[{}] findAll -> {} records", entityClass.getSimpleName(), results.size());
            return results;
        }
    }

    public List<T> findBy(String fieldName, Object value) {
        log.info("[{}] findBy {} = {}", entityClass.getSimpleName(), fieldName, value);
        try (EntityManager em = jpaUtil.em()) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
            cq.select(root).where(cb.equal(root.get(fieldName), value));
            List<T> results = em.createQuery(cq).getResultList();
            log.info("[{}] findBy {} -> {} records", entityClass.getSimpleName(), fieldName, results.size());
            return results;
        }
    }

    public T save(T entity) {
        log.info("[{}] save: {}", entityClass.getSimpleName(), entity);
        EntityManager em = jpaUtil.em();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            log.info("[{}] save OK", entityClass.getSimpleName());
            return entity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.warn("[{}] save FAILED, rolled back: {}", entityClass.getSimpleName(), e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public T update(T entity) {
        log.info("[{}] update: {}", entityClass.getSimpleName(), entity);
        EntityManager em = jpaUtil.em();
        try {
            em.getTransaction().begin();
            T merged = em.merge(entity);
            em.getTransaction().commit();
            log.info("[{}] update OK", entityClass.getSimpleName());
            return merged;
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.warn("[{}] update FAILED, rolled back: {}", entityClass.getSimpleName(), e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean delete(ID id) {
        log.info("[{}] delete id: {}", entityClass.getSimpleName(), id);
        EntityManager em = jpaUtil.em();
        try {
            T entity = em.find(entityClass, id);
            if (entity == null) {
                log.warn("[{}] delete -> id {} not found, skipping", entityClass.getSimpleName(), id);
                return false;
            }
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
            log.info("[{}] delete OK", entityClass.getSimpleName());
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            log.warn("[{}] delete FAILED, rolled back: {}", entityClass.getSimpleName(), e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public long count() {
        log.info("[{}] count", entityClass.getSimpleName());
        try (EntityManager em = jpaUtil.em()) {
            String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
            long n = em.createQuery(jpql, Long.class).getSingleResult();
            log.info("[{}] count -> {}", entityClass.getSimpleName(), n);
            return n;
        }
    }
}