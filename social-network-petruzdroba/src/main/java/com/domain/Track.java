package main.java.com.domain;

public abstract class Track {
    protected final int distanta;
    protected final int id;

    public Track(int distanta, int id) {
        this.distanta = distanta;
        this.id = id;
    }

    public abstract int getDistanta();

    public abstract int getId();
}
