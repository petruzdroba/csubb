package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Configuration;
import com.zdroba.mpp.entity.Game;

import java.util.List;

public interface IGameService {

    Game start(Long id);

    Game get(Long id);

    Game addPlayer(Long userId);

    void nextTurn(Long gameId);
    void checkEnd(Long gameId);

    int action(Long userId, Long gameId);

    void setConfiguration(Long gameId, Long configurationId);

    Configuration getConfiguration(Long gameId);

    Configuration addConfiguration(List<Integer> board);

    List<Configuration> getConfigurations();

    List<Game> getGamesWithAtLeastPoints(Long userId, int minPoints);
}
