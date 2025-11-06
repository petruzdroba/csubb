package main.java.com.domain;

import java.util.Arrays;

public class RaceEvent extends Event{
    private OptimiserResult raceResult;

    public RaceEvent() {
        raceResult = null;
    }

    @Override
    public String notification() {
        if (raceResult == null) {
            return "Race result not yet available.";
        }
        return " notified, time: " + String.format("%.2f", raceResult.getBestTime())
                + " " + Arrays.toString(raceResult.getBestAssignment());
    }


    public void setRaceResult(OptimiserResult result){
        this.raceResult = result;
    }
}
