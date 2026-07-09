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

    @ElementCollection
    private List<Integer> positions = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "configuration_id")
    private Configuration configuration;

    private boolean started = false;
    private boolean finished = false;
    private int currentTurnIndex = 0;

    private int totalMoves = 0;

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

    public List<Integer> getPositions() {
        return positions;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }
}
