package com.zdroba.mpp.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<Long> players = new ArrayList<>();

    @ElementCollection
    private List<Integer> points = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="configuration_id")
    private Configuration configuration;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Guess> guesses = new ArrayList<>();

    private boolean started = false;
    private boolean finished = false;
    private int currentTurnIndex = 0;

    public int turnCount = 0;

    public Long getCurrentPlayerId() {
        if (players.isEmpty()) return null;
        return players.get(currentTurnIndex);
    }

    public Long getId() {
        return id;
    }

    public List<Long> getPlayers() {
        return players;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getCurrentTurnIndex() {
        return currentTurnIndex;
    }

    public void setCurrentTurnIndex(int currentTurnIndex) {
        this.currentTurnIndex = currentTurnIndex;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
