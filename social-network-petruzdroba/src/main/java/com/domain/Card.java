package main.java.com.domain;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private long id;
    private String numeCard;
    private List<Duck> membri;

    public Card(long id, String numeCard) {
        this.id = id;
        this.numeCard = numeCard;
        this.membri = new ArrayList<Duck>();
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
}
