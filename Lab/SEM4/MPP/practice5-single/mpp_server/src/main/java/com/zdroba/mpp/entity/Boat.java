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

    private int x_1;
    private int y_1;

    private int x_2;
    private int y_2;

    private int x_3;
    private int y_3;

    private boolean guessed1 = false;
    private boolean guessed2 = false;
    private boolean guessed3 = false;


    public Boat() {
    }

    public Boat(int x_1, int y_1, int x_2, int y_2, int x_3, int y_3) {
        this.x_1 = x_1;
        this.y_1 = y_1;
        this.x_2 = x_2;
        this.y_2 = y_2;
        this.x_3 = x_3;
        this.y_3 = y_3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getX_3() {
        return x_3;
    }

    public void setX_3(int x_3) {
        this.x_3 = x_3;
    }

    public int getY_3() {
        return y_3;
    }

    public void setY_3(int y_3) {
        this.y_3 = y_3;
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

    public boolean isGuessed3() {
        return guessed3;
    }

    public void setGuessed3(boolean guessed3) {
        this.guessed3 = guessed3;
    }
}
