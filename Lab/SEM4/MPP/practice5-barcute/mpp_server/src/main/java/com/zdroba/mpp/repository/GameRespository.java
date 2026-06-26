package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Game;
import org.springframework.stereotype.Repository;

@Repository
public class GameRespository extends GenericRepo<Game, Long>{
    public GameRespository() {
        super(Game.class);
    }
}
