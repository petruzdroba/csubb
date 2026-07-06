package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Game;

import java.util.List;

public interface IGameService {

    Game get(Long gameId);

    Game start(Long gameId);

    void turn(Long gameId);

    Game addPlayer(Long userId,  int x_1, int y_1, int x_2, int y_2);

    boolean addGuess(Long userId, Long gameId, int x, int y);

    List<Game> getGames(Long userId);
}
