package com.zdroba.mpp.entity;

public class Guess {

    private Long id;

    private int points;

    private Type type;

    public Guess() {
    }

    public Guess(int points,Type type ) {
        this.points = points;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
