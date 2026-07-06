package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Participant;
import com.zdroba.mpp.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParticipantRepo implements IParticipantRepo {

    private final JpaUtil jpaUtil;

    public ParticipantRepo(JpaUtil jpaUtil) {
        this.jpaUtil = jpaUtil;
    }

    @Override
    public List<Participant> get() {
        try (EntityManager em = jpaUtil.em()) {
            return em.createQuery(
                    "SELECT p FROM Participant p ORDER BY p.id DESC",
                    Participant.class
            ).getResultList();
        }
    }

    @Override
    public Participant get(Long id) {
        try (EntityManager em = jpaUtil.em()) {
            return em.find(Participant.class, id);
        }
    }

    @Override
    public void modify(Participant participant) {
        try (EntityManager em = jpaUtil.em()) {
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();
                em.merge(participant);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        }
    }
}