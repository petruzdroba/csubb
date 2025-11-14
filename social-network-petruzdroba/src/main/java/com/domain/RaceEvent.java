package com.domain;

import com.containers.DuckRaceContainer;
import com.optimisers.BacktrackOptimiser;

public class RaceEvent extends Event{
    private OptimiserResult raceResult;
    private final DuckRaceContainer container;
    private final BacktrackOptimiser optimiser;

    public RaceEvent(long id, DuckRaceContainer container) {
        super(id);
        this.container = container;
        optimiser = new BacktrackOptimiser(container);
        raceResult = null;
    }

    @Override
    public void start() {
        raceResult = optimiser.findMinimumTime();
    }

    @Override
    public String notification() {
        if (raceResult == null) {
            return "Race result not yet available.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" notified, time: ").append(String.format("%.2f", raceResult.getBestTime())).append("\n");

        int[] assignment = raceResult.getBestAssignment();
        for (int lane = 0; lane < assignment.length; lane++) {
            sb.append("Lane ").append(lane + 1).append(": Duck ").append(assignment[lane]).append("\n");
        }

        return sb.toString().trim();
    }


    public void setRaceResult(OptimiserResult result){
        this.raceResult = result;
    }

    public DuckRaceContainer getContainer() {
        return container;
    }
}
