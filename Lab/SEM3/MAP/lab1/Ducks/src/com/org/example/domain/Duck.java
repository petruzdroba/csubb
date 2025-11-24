package com.org.example.domain;

public class Duck extends Racer {
    private final int rezistenta;

    public Duck(int rezistenta, int viteza, int id) {
        super(viteza, id);
        this.rezistenta = rezistenta;
    }

    public int getRezistenta() {
        return rezistenta;
    }

    public int getViteza() {
        return viteza;
    }

    public int getId(){return  id;}
}
