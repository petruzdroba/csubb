package com.zdroba.mpp.entity;

import jakarta.persistence.*;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @OneToOne(cascade = CascadeType.ALL)
    private Boat boats;

    private int turns = 0;

    private int points = 0;

    public Game(Long userId, Boat boats) {
        this.userId = userId;
        this.boats = boats;
    }

    public Game() {
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

    public Boat getBoats() {
        return boats;
    }

    public void setBoats(Boat boats) {
        this.boats = boats;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addTurns(){
        turns += 1;
    }

    public void addPoints(int points){
        this.points += points;
    }
}
