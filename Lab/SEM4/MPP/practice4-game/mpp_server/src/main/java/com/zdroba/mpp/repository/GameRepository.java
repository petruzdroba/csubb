package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.utils.JpaUtil;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository extends GenericRepo<Game, Long>{
    public GameRepository(JpaUtil jpaUtil) {
        super(Game.class, jpaUtil);
    }
}
