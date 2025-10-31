package main.java.com.domain;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private long id;
    private String numeCard;
    private List<Long> membri;

    public Card(long id, String numeCard) {
        this.id = id;
        this.numeCard = numeCard;
        this.membri = new ArrayList<Long>();
    }

    public double getPerformantaMedie(){
        //media vitezelor si rezistentelotr
        return 0.0;
    }

    public long getId() {
        return id;
    }

    public String getNumeCard() {
        return numeCard;
    }

    public void addDuck(long duckId){
        membri.add(duckId);
    }

    public List<Long> getMembri() {
        return membri;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", numeCard='" + numeCard + '\'' +
                ", membri=" + membri +
                '}';
    }
}
