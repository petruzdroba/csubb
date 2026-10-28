package org.example.strategy;

import org.example.MatrixMultiplier;

public class ColumnMajorBlockStrategy implements DistributionStrategy{

    @Override
    public void compute(int[][] a, int[][] b, int[][] result, int threadId, int nrThreads) {
        int rows = result.length;
        int cols = result[0].length;
        int total = rows * cols;

        int start = threadId * total / nrThreads;
        int end = (threadId + 1) * total / nrThreads;

        for(int i = start ; i < end ; i++) {
            int row = i % rows;
            int col = i / rows;

            MatrixMultiplier.computeElement(a, b, result, row, col, threadId);
        }
    }
}
