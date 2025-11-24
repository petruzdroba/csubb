package com.org.example.logic;

import com.org.example.containers.RaceTrackContainer;

public abstract class Optimiser {
    protected final RaceTrackContainer container;

    public Optimiser(RaceTrackContainer container) {
        this.container = container;
    }

    public abstract OptimiserResult findMinimumTime();
}
