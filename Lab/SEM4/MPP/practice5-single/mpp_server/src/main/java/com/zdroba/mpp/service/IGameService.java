package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.entity.Guess;

public interface IGameService {

    Game get(Long id);

    Game start(Long userId);

    Guess guess(Long userId, Long gameId, int x, int y);
}
