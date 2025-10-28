package main.java.com.domain;

public class Duck extends User{
    public enum TipRata{
        FLYING, SWIMMING, FLYING_AND_SWIMMING
    }

    private TipRata tip;
    private double viteza;
    private double rezistenta;
    private long cardId;

    public Duck(long id, String username, String email, String password, TipRata tip, double viteza, double rezistenta, long cardId) {
        super(id, username, email, password);
        this.tip = tip;
        this.viteza = viteza;
        this.rezistenta = rezistenta;
        this.cardId = cardId;
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

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    @Override
    public String toString() {
        return super.toString() +"Duck{" +
                "tip=" + tip +
                ", viteza=" + viteza +
                ", rezistenta=" + rezistenta +
                ", card=" + cardId +
                '}';
    }
}
