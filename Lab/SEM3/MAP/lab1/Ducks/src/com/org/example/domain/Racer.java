package com.org.example.domain;

public abstract class Racer{
    protected final int viteza;
    protected final int id;

    public Racer(int viteza, int id) {
        this.viteza = viteza;
        this.id = id;
    }

    public abstract int getViteza();

    public abstract int getId();
}
