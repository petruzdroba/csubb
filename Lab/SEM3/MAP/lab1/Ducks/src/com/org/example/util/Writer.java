package com.org.example.util;

import com.org.example.containers.RaceTrackContainer;
import com.org.example.domain.Racer;
import com.org.example.logic.OptimiserResult;

public abstract class Writer {
    public void write(OptimiserResult result){
        System.out.println(result.getBestTime());

        for (int i = 0; i < result.getBestAssignment().length; i++) {
            int racerIdx = result.getBestAssignment()[i] + 1;
            int trackIdx = i + 1;
            System.out.printf(String.format( "Track %d -> Racer %s%n", trackIdx, racerIdx));

        }
    }
}
