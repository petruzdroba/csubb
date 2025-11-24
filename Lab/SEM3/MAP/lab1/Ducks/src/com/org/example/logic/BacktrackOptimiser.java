package com.org.example.logic;

import com.org.example.containers.RaceTrackContainer;
import com.org.example.domain.Racer;
import com.org.example.domain.Track;

public class BacktrackOptimiser extends Optimiser {
    public BacktrackOptimiser(RaceTrackContainer container) {
        super(container);
    }

    @Override
    public OptimiserResult findMinimumTime() {
        /*
         * Gaseste timpul minim pentru o cursa
         * */
        Track[] tracks = container.getTracks();
        Racer[] racers = container.getRacers();
        int N = container.getRacerCount();
        int M = container.getTrackCount();

        int[] chosen = new int[M];
        double[] bestTime = new double[] { Double.POSITIVE_INFINITY };

        int[] bestAssignment = new int[M];

        backtrack(racers, tracks, N, M, 0, 0, chosen, bestTime, bestAssignment);

        return new OptimiserResult((bestTime[0] * 100.0) / 100.0, bestAssignment);
    }

    private void backtrack(Racer[] racers, Track[] tracks, int N, int M,
                           int start, int depth, int[] chosen, double[] bestTime, int[] bestAssignment) {
        /*
        * Cauta toate aranjamentele posibile de M rate, respectand ordiena din container (pt duckcontainer dupa rezistenta reverse)
        * */
        if (depth == M) {
            double maxTime = 0.0;

            for (int i = 0; i < M; i++) {
                int racerIdx = chosen[M - 1 - i];
                double timeNeeded = 2.0 * tracks[i].getDistanta() / racers[racerIdx].getViteza();

                if (timeNeeded > maxTime) {
                    maxTime = timeNeeded;
                }
            }

            if (maxTime < bestTime[0]) {
                bestTime[0] = maxTime;
                System.arraycopy(chosen, 0, bestAssignment, 0, M);
            }
            return;
        }

        // daca nu se umple
        int need = M - depth;
        for (int i = start; i <= N - need; i++) {
            chosen[depth] = i;
            backtrack(racers, tracks, N, M, i + 1, depth + 1, chosen, bestTime, bestAssignment);
        }
    }
}