package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Game;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepo extends GenericRepo<Game, Long>{
    public GameRepo() {
        super(Game.class);
    }
}
