package com.zdroba.mpp.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> players = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Word> words = new ArrayList<>();

    public Long getId() { return id; }
    public List<Long> getPlayers() { return players; }
    public List<Word> getWords() { return words; }
    public void addPlayer(Long u) { if (!players.contains(u)) players.add(u); }
    public void addWord(Word w) { words.add(w); }
    public void setWords(List<Word> w) { this.words = w; }
    public boolean isComplete() {
        return words.stream().allMatch(w ->
                Character.isUpperCase(w.getFirst()) &&
                        Character.isUpperCase(w.getSecond()) &&
                        Character.isUpperCase(w.getThird()));
    }
}