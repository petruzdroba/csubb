package com.zdroba.mpp.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GenericRepo<T, ID> {

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private final Class<T> entityClass;

    @PersistenceContext
    private EntityManager em;

    public GenericRepo(Class<T> entityClass) {
        this.entityClass = entityClass;
        log.info("GenericRepo ready for: {}", entityClass.getSimpleName());
    }

    public Optional<T> findById(ID id) {
        log.info("[{}] findById: {}", entityClass.getSimpleName(), id);
        Optional<T> result = Optional.ofNullable(em.find(entityClass, id));
        log.info("[{}] findById {} -> {}", entityClass.getSimpleName(), id, result.isPresent() ? "found" : "not found");
        return result;
    }

    public List<T> findAll() {
        log.info("[{}] findAll", entityClass.getSimpleName());
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        List<T> results = em.createQuery(jpql, entityClass).getResultList();
        log.info("[{}] findAll -> {} records", entityClass.getSimpleName(), results.size());
        return results;
    }

    public List<T> findBy(String fieldName, Object value) {
        log.info("[{}] findBy {} = {}", entityClass.getSimpleName(), fieldName, value);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root).where(cb.equal(root.get(fieldName), value));
        List<T> results = em.createQuery(cq).getResultList();
        log.info("[{}] findBy {} -> {} records", entityClass.getSimpleName(), fieldName, results.size());
        return results;
    }

    @Transactional
    public T save(T entity) {
        log.info("[{}] save: {}", entityClass.getSimpleName(), entity);
        em.persist(entity);
        em.flush();
        log.info("[{}] save OK", entityClass.getSimpleName());
        return entity;
    }

    @Transactional
    public T update(T entity) {
        log.info("[{}] update: {}", entityClass.getSimpleName(), entity);
        T merged = em.merge(entity);
        em.flush();
        log.info("[{}] update OK", entityClass.getSimpleName());
        return merged;
    }

    @Transactional
    public boolean delete(ID id) {
        log.info("[{}] delete id: {}", entityClass.getSimpleName(), id);
        T entity = em.find(entityClass, id);
        if (entity == null) {
            log.warn("[{}] delete -> id {} not found, skipping", entityClass.getSimpleName(), id);
            return false;
        }
        em.remove(entity);
        em.flush();
        log.info("[{}] delete OK", entityClass.getSimpleName());
        return true;
    }

    public long count() {
        log.info("[{}] count", entityClass.getSimpleName());
        String jpql = "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e";
        long n = em.createQuery(jpql, Long.class).getSingleResult();
        log.info("[{}] count -> {}", entityClass.getSimpleName(), n);
        return n;
    }
}

