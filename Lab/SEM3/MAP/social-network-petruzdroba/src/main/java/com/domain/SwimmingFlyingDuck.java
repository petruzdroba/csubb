package com.domain;

public class SwimmingFlyingDuck extends Duck implements Inotator, Zburator{
    public SwimmingFlyingDuck(long id, String username, String email, String password, TipRata tip, double viteza, double rezistenta) {
        super(id, username, email, password, tip, viteza, rezistenta);
    }

    @Override
    public void inoata() {

    }

    @Override
    public void zboara() {

    }
}
