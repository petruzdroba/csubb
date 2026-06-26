package com.zdroba.mpp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Status status;

    private String name;

    private Integer scoreExec;
    private Integer scoreTeh;
    private Integer scoreArt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScoreExec() {
        return scoreExec;
    }

    public void setScoreExec(Integer scoreExec) {
        this.scoreExec = scoreExec;
    }

    public Integer getScoreTeh() {
        return scoreTeh;
    }

    public void setScoreTeh(Integer scoreTeh) {
        this.scoreTeh = scoreTeh;
    }

    public Integer getScoreArt() {
        return scoreArt;
    }

    public void setScoreArt(Integer scoreArt) {
        this.scoreArt = scoreArt;
    }

    public Integer total(){
        return this.scoreTeh + this.scoreExec + this.scoreArt;
    }
}
