package com.zdroba.mpp.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name="children")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long checkpointId;

    private Instant hour;

    public Child(Long id, String name, Long checkpointId, Instant hour) {
        this.id = id;
        this.name = name;
        this.checkpointId = checkpointId;
        this.hour = hour;
    }

    public Child() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(Long checkpointId) {
        this.checkpointId = checkpointId;
    }

    public Instant getHour() {
        return hour;
    }

    public void setHour(Instant hour) {
        this.hour = hour;
    }
}
