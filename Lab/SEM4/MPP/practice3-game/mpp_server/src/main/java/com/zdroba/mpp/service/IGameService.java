package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Game;

import java.util.Map;

public interface IGameService {
    Game start();

    Game get();

    void addPlayer(Long userId, String word);

    int guess(Long userId, Long wordId, char letter);

    Map<String, Object> getPlayerGuesses(Long gameId, Long userId);
}
