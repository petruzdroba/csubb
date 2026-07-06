package org.zdroba.entity;

public class RaceEvent {
    private Long id;
    private int engine;

    public RaceEvent(int engine) {
        this.engine = engine;
    }

    public RaceEvent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEngine() {
        return engine;
    }

    public void setEngine(int engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return engine+"cc";
    }
}
