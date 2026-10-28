package org.example.strategy;

import org.example.MatrixMultiplier;

public class RowMajorBlockStrategy implements DistributionStrategy{

    @Override
    public void compute(int[][] a, int[][] b, int[][] result, int threadId, int nrThreads) {
        int rows = result.length;
        int cols = result[0].length;
        int total = rows * cols; // total elements to compute, cells

        int start = threadId * total / nrThreads;
        int end = (threadId + 1) * total / nrThreads;

        for(int i = start; i < end; i++) {
            int row = i / cols;
            int col = i % cols;

            MatrixMultiplier.computeElement(a, b, result, row, col, threadId);
        }
    }
}
