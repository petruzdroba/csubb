package com.zdroba.mpp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Guess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long gameId;
    private Long userId;
    private Long wordId;
    private char letter;

    public Guess() {}
    public Guess(Long gameId, Long userId, Long wordId, char letter) {
        this.gameId = gameId;
        this.userId = userId;
        this.wordId = wordId;
        this.letter = letter;
    }

    public Long getId() { return id; }
    public Long getGameId() { return gameId; }
    public Long getUserId() { return userId; }
    public Long getWordId() { return wordId; }
    public char getLetter() { return letter; }
}