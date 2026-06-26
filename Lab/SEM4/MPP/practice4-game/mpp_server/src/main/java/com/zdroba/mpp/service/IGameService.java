package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.entity.Points;

import java.util.List;

public interface IGameService {

    Game start();

    Game get();

    void addPlayer(Long userId);



    int addTom(Long userId,String tara, String oras, String mare);

    List<Points>  summary(Long gameId);

    List<Points> summary(Long gameId, Long userId);
}
