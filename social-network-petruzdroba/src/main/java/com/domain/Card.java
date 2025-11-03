package main.java.com.domain;

import java.util.ArrayList;
import java.util.List;

public class Card<T extends Duck> {
    private long id;
    private String numeCard;
    private List<T> membri;

    public Card(long id, String numeCard) {
        this.id = id;
        this.numeCard = numeCard;
        this.membri = new ArrayList<T>();
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

    public void addDuck(T duck){
        membri.add(duck);
    }

    public List<T> getMembri() {
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
