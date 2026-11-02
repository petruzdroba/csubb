package org.example.strategy;

import org.example.MatrixMultiplier;

public class CyclicStrategy2 implements DistributionStrategy {
    
    @Override
    public void compute(int[][] a, int[][] b, int[][] result, int threadId, int nrThreads) {
        int rows = result.length;
        int cols = result[0].length;
        int total = rows * cols;
        
        for (int i = 0; i < total; i++) {
            if (i % nrThreads == threadId) {
                int row = i / cols;
                int col = i % cols;
                MatrixMultiplier.computeElement(a, b, result, row, col, threadId);
            }
        }
    }
}
