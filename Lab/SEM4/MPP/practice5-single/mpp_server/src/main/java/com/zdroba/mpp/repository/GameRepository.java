package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Game;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository extends GenericRepo<Game, Long> {
    public GameRepository() {
        super(Game.class);
    }
}
