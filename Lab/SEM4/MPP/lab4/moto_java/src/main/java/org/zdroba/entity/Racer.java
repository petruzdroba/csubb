package org.zdroba.entity;

public class Racer {
    private Long id;
    private String name;
    private String cnp;
    private RaceEvent engine;
    private Team team;

    public Racer(String name, String cnp, RaceEvent engine) {
        this.name = name;
        this.cnp = cnp;
        this.engine = engine;
        this.team = Team.NONE;
    }

    public Racer(String name, String cnp, Team team, RaceEvent engine) {
        this.name = name;
        this.cnp = cnp;
        this.team = team;
        this.engine = engine;
    }

    public Racer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public RaceEvent getEngine() {
        return engine;
    }

    public void setEngine(RaceEvent engine) {
        this.engine = engine;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
