package main.java.com.domain;

public class Duck extends User{
    public enum TipRata{
        FLYING, SWIMMING, FLYING_AND_SWIMMING
    }

    private TipRata tip;
    private double viteza;
    private double rezistenta;
    private Card card;

    public Duck(long id, String username, String email, String password, TipRata tip, double viteza, double rezistenta, Card card) {
        super(id, username, email, password);
        this.tip = tip;
        this.viteza = viteza;
        this.rezistenta = rezistenta;
        this.card = card;
    }

    public TipRata getTip() {
        return tip;
    }

    public void setTip(TipRata tip) {
        this.tip = tip;
    }

    public double getViteza() {
        return viteza;
    }

    public void setViteza(double viteza) {
        this.viteza = viteza;
    }

    public double getRezistenta() {
        return rezistenta;
    }

    public void setRezistenta(double rezistenta) {
        this.rezistenta = rezistenta;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
