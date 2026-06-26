package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Guess;
import com.zdroba.mpp.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GuessRepository extends GenericRepo<Guess, Long> {
    public GuessRepository(JpaUtil jpaUtil) {
        super(Guess.class, jpaUtil);
    }

    public List<Guess> findByGameAndUser(Long gameId, Long userId) {
        try (EntityManager em = jpaUtil.em()) {
            return em.createQuery(
                            "SELECT g FROM Guess g WHERE g.gameId = :gameId AND g.userId = :userId", Guess.class)
                    .setParameter("gameId", gameId)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }
}
