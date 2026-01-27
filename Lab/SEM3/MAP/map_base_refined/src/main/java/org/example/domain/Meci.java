package org.example.domain;

public class Meci {
    private Long id;
    private String numeGazda;
    private String numeOaspete;
    private int scorGazda;
    private int scorOaspete;

    public Meci(Long id, String numeGazda, String numeOaspete, int scorGazda, int scorOaspete) {
        this.id = id;
        this.numeGazda = numeGazda;
        this.numeOaspete = numeOaspete;
        this.scorGazda = scorGazda;
        this.scorOaspete = scorOaspete;
    }

    public Meci() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeGazda() {
        return numeGazda;
    }

    public void setNumeGazda(String numeGazda) {
        this.numeGazda = numeGazda;
    }

    public String getNumeOaspete() {
        return numeOaspete;
    }

    public void setNumeOaspete(String numeOaspete) {
        this.numeOaspete = numeOaspete;
    }

    public int getScorGazda() {
        return scorGazda;
    }

    public void setScorGazda(int scorGazda) {
        this.scorGazda = scorGazda;
    }

    public int getScorOaspete() {
        return scorOaspete;
    }

    public void setScorOaspete(int scorOaspete) {
        this.scorOaspete = scorOaspete;
    }

    @Override
    public String toString() {
        return String.format("OASPETE %s       %d - %d      %s GAZDA", numeOaspete, scorOaspete, scorGazda, numeGazda);
    }
}
