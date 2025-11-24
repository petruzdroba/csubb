package com.org.example.containers;

import com.org.example.domain.Duck;
import com.org.example.domain.Racer;
import com.org.example.domain.Track;

import java.util.Arrays;
import java.util.Comparator;

public class DuckRaceContainer extends RaceTrackContainer {

    public DuckRaceContainer(int racerCount, int trackCount, Duck[] racers, Track[] tracks) {
        super(racerCount, trackCount, racers, tracks);
        sortByResistance();
    }

    @Override
    public Racer[] getRacers() {
        return super.racers;
    }

    @Override
    public int getRacerCount() {
        return super.racerCount;
    }

    @Override
    public Track[] getTracks() {
        return super.tracks;
    }

    @Override
    public int getTrackCount() {
        return super.trackCount;
    }

    private void sortByResistance(){
        Arrays.sort((Duck[]) racers, Comparator.comparingInt(Duck::getRezistenta).reversed());
    }
}
