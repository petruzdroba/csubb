package com.zdroba.mpp.entity;

import jakarta.persistence.*;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long player1;
    private Long player2;

    @OneToOne(cascade = CascadeType.ALL)
    private Boat boat1;

    @OneToOne(cascade = CascadeType.ALL)
    private Boat boat2;

    private int turn;

    private Status status;

    private int try1 = 0;
    private int try2 = 0;

    private Long winner = -1L;

    public Game() {
        this.turn = 1;
    }

    public Game(Long player1, Long player2, Boat boat1, Boat boat2) {
        turn = 1;
        this.player1 = player1;
        this.player2 = player2;
        this.boat1 = boat1;
        this.boat2 = boat2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayer1() {
        return player1;
    }

    public void setPlayer1(Long player1) {
        this.player1 = player1;
    }

    public Long getPlayer2() {
        return player2;
    }

    public void setPlayer2(Long player2) {
        this.player2 = player2;
    }

    public Boat getBoat1() {
        return boat1;
    }

    public void setBoat1(Boat boat1) {
        this.boat1 = boat1;
    }

    public Boat getBoat2() {
        return boat2;
    }

    public void setBoat2(Boat boat2) {
        this.boat2 = boat2;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTry1() {
        return try1;
    }

    public void setTry1(int try1) {
        this.try1 = try1;
    }

    public int getTry2() {
        return try2;
    }

    public void setTry2(int try2) {
        this.try2 = try2;
    }

    public void add1(){
        try1 += 1;
    }

    public void add2(){
        try2 += 2;
    }

    public Long getWinner() {
        return winner;
    }

    public void setWinner(Long winner) {
        this.winner = winner;
    }
}
