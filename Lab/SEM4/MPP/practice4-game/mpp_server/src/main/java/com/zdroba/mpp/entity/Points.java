package com.zdroba.mpp.entity;

import jakarta.persistence.*;

@Entity
public class Points {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long gameId;

    private int points;

    private short turn;

    public Points() {
    }

    public Points(Long userId, Long gameId, int points, short turn) {
        this.userId = userId;
        this.gameId = gameId;
        this.points = points;
        this.turn = turn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public short getTurn() {
        return turn;
    }

    public void setTurn(short turn) {
        this.turn = turn;
    }
}
