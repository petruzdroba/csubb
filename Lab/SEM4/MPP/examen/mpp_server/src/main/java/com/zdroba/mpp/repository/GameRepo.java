package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.entity.Guess;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class GameRepo extends GenericRepo<Game, Long>{
    public GameRepo() {
        super(Game.class);
    }

    public List<Game> getByUserIdAnd(Long userId, Integer wordCount){
        return this.findAll().stream().filter(game -> {
            List<Guess> guesses = game.guesses.stream().filter(g -> Objects.equals(g.userId, userId)).toList();

            return game.getPlayers().contains(userId) && guesses.size() >= wordCount;
        }).toList();
    }
}
