package org.example.strategy;

public interface DistributionStrategy {
    void compute(int[][] a, int[][] b, int[][] result, int threadId, int nrThreads);
}
