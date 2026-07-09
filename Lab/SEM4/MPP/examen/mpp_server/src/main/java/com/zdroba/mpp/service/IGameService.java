package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Configuration;
import com.zdroba.mpp.entity.Game;

import java.util.List;

public interface IGameService {

    Game start(Long id);

    Game get(Long id);

    Game addPlayer(Long userId);

    void nextTurn(Long gameId);
    boolean checkEnd(Long gameId);

    int action(Long userId, Long gameId, String guess);

    Configuration getConfig(Long gameId);

    void addConfig(Long gameId, Long confId);

    List<Game> getByUserAndY(Long userId, Integer Y);
}
