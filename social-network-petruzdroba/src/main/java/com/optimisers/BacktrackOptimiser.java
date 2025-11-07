package main.java.com.optimisers;

import main.java.com.containers.DuckRaceContainer;
import main.java.com.domain.Culoar;
import main.java.com.domain.Duck;
import main.java.com.domain.OptimiserResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BacktrackOptimiser {

    private final DuckRaceContainer container;

    public BacktrackOptimiser(DuckRaceContainer container) {
        this.container = container;
    }

    public OptimiserResult findMinimumTime() {
        Collection<Culoar> culoareCol = container.getCuloare();
        Collection<Duck> ducksCol = container.getDucks();

        List<Culoar> culoare = new ArrayList<>(culoareCol);
        List<Duck> ducks = new ArrayList<>(ducksCol);

        int N = ducks.size();
        int M = culoare.size();

        int[] chosen = new int[M];
        double[] bestTime = new double[] { Double.POSITIVE_INFINITY };
        int[] bestAssignment = new int[M];

        backtrack(ducks, culoare, N, M, 0, 0, chosen, bestTime, bestAssignment);

        return new OptimiserResult((bestTime[0] * 100.0) / 100.0, bestAssignment);
    }

    private void backtrack(List<Duck> ducks, List<Culoar> culoare, int N, int M,
                           int start, int depth, int[] chosen, double[] bestTime, int[] bestAssignment) {

        if (depth == M) {
            double maxTime = 0.0;

            for (int i = 0; i < M; i++) {
                int duckIdx = chosen[M - 1 - i];
                double timeNeeded = 2.0 * culoare.get(i).getDistanta() / ducks.get(duckIdx).getViteza();

                if (timeNeeded > maxTime) {
                    maxTime = timeNeeded;
                }
            }

            if (maxTime < bestTime[0]) {
                bestTime[0] = maxTime;
                for (int i = 0; i < M; i++) {
                    int chosenIdx = chosen[i];
                    bestAssignment[i] = Math.toIntExact(ducks.get(chosenIdx).getId());
                }

            }
            return;
        }

        int need = M - depth;
        for (int i = start; i <= N - need; i++) {
            chosen[depth] = i;
            backtrack(ducks, culoare, N, M, i + 1, depth + 1, chosen, bestTime, bestAssignment);
        }
    }
}
