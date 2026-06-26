package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Points;
import com.zdroba.mpp.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointsRepository extends GenericRepo<Points, Long>{
    public PointsRepository( JpaUtil jpaUtil) {
        super(Points.class, jpaUtil);
    }

    public List<Points> findByGameAndUser(Long gameId, Long userId){
        try(EntityManager em = jpaUtil.em()){
            return em.createQuery("SELECT p FROM Points p WHERE p.gameId = :gameId AND p.userId= :userId", Points.class)
                    .setParameter("gameId", gameId)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }
}
