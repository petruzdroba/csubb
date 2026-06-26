package com.zdroba.mpp.entity;

import jakarta.persistence.*;

@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private char first, second, third;

    public Word() {}
    public Word(Long userId, char a, char b, char c) {
        this.userId = userId;
        this.first = a; this.second = b; this.third = c;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public char getFirst() { return first; }
    public char getSecond() { return second; }
    public char getThird() { return third; }
    public void setFirst(char c) { this.first = c; }
    public void setSecond(char c) { this.second = c; }
    public void setThird(char c) { this.third = c; }
}
