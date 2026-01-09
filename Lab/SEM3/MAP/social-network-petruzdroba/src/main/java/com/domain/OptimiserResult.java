package com.domain;

public class OptimiserResult {
    private final double bestTime;
    private final int[] bestAssignment;

    public OptimiserResult(double bestTime, int[] bestAssignment) {
        this.bestTime = bestTime;
        this.bestAssignment = bestAssignment;
    }

    public double getBestTime() {
        return bestTime;
    }

    public int[] getBestAssignment() {
        return bestAssignment;
    }
}
