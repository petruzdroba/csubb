package org.example.strategy;

import org.example.MatrixMultiplier;

public class CyclicStrategy implements DistributionStrategy {

    @Override
    public void compute(int[][] a, int[][] b, int[][] result, int threadId, int nrThreads) {
        int rows = result.length;
        int cols = result[0].length;
        int total = rows * cols;

        for (int i = threadId; i < total; i += nrThreads) {
            int row = i / cols;
            int col = i % cols;
            MatrixMultiplier.computeElement(a, b, result, row, col, threadId);
        }
    }
}
