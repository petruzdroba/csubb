package com.zdroba.mpp.repository;

import com.zdroba.mpp.entity.Game;

public interface IGameRepo {

    Game save(Game game);
    
    Game update(Game game);
}
