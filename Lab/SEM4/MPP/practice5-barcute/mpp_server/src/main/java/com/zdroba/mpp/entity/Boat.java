package com.zdroba.mpp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Boat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int x_1; // marked -1 if guessed
    private int y_1;
    private int x_2;
    private int y_2;

    private boolean guessed1 = false;
    private boolean guessed2= false;

    public Boat( Long userId, int x_1, int y_1, int x_2, int y_2) {
        this.userId = userId;
        this.x_1 = x_1;
        this.y_1 = y_1;
        this.x_2 = x_2;
        this.y_2 = y_2;
    }

    public Boat() {
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

    public int getX_1() {
        return x_1;
    }

    public void setX_1(int x_1) {
        this.x_1 = x_1;
    }

    public int getY_1() {
        return y_1;
    }

    public void setY_1(int y_1) {
        this.y_1 = y_1;
    }

    public int getX_2() {
        return x_2;
    }

    public void setX_2(int x_2) {
        this.x_2 = x_2;
    }

    public int getY_2() {
        return y_2;
    }

    public void setY_2(int y_2) {
        this.y_2 = y_2;
    }

    public boolean isGuessed1() {
        return guessed1;
    }

    public void setGuessed1(boolean guessed1) {
        this.guessed1 = guessed1;
    }

    public boolean isGuessed2() {
        return guessed2;
    }

    public void setGuessed2(boolean guessed2) {
        this.guessed2 = guessed2;
    }
}
