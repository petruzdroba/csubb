package org.zdroba.entity;

import jakarta.persistence.*;

@Entity
@Table(name="events")
public class RaceEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public  String toJson() {
        return "{ \"id\": " + this.id + ", \"engine\": " + this.engine + " }";
    }
}
