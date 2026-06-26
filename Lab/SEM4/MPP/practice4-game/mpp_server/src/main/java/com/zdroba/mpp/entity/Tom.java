package com.zdroba.mpp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Tom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String tara;
    private String oras;
    private String mare;

    public Long getId() {
        return id;
    }

    public Tom() {
    }

    public Tom(Long userId, String tara, String oras, String mare) {
        this.userId = userId;
        this.tara = tara;
        this.oras = oras;
        this.mare = mare;
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

    public String getTara() {
        return tara;
    }

    public void setTara(String tara) {
        this.tara = tara;
    }

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public String getMare() {
        return mare;
    }

    public void setMare(String mare) {
        this.mare = mare;
    }
}
