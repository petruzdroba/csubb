package com.org.example.containers;

import com.org.example.domain.Racer;
import com.org.example.domain.Track;

public abstract class RaceTrackContainer {
    protected Racer[] racers;
    protected final int racerCount;
    protected Track[] tracks;
    protected final int trackCount;

    public RaceTrackContainer(int racerCount, int trackCount, Racer[] racers, Track[] tracks) {
        this.racerCount = racerCount;
        this.trackCount = trackCount;
        this.racers = racers;
        this.tracks = tracks;
    }

    public abstract Racer[] getRacers();

    public abstract int getRacerCount();

    public abstract  Track[] getTracks();

    public abstract  int getTrackCount();
}
