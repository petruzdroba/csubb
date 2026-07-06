package com.zdroba.mpp.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> players = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Tom> toms = new ArrayList<>();

    private char currentLetter;

    private short turn;

    public Game() {
        this.currentLetter = (char)('A' + new Random().nextInt(26));
        this.turn = 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getPlayers() {
        return players;
    }

    public void setPlayers(List<Long> players) {
        this.players = players;
    }

    public List<Tom> getToms() {
        return toms;
    }

    public void setToms(List<Tom> toms) {
        this.toms = toms;
    }

    public short getTurn() {
        return turn;
    }

    public void setTurn(short turn) {
        this.turn = turn;
    }

    public char getCurrentLetter() {
        return currentLetter;
    }

    public void setCurrentLetter(char currentLetter) {
        this.currentLetter = currentLetter;
    }

    public void addPlayer(Long userId) {
        this.players.add(userId);
    }
}
