package main.java.com.domain;

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
}
