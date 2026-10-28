package org.example;

import org.example.strategy.DistributionStrategy;

public class MatrixTask implements Runnable{

    private final int[][] a;
    private final int[][] b;
    private final int[][] result;
    private final int threadId;
    private final int nrThreads;
    private final DistributionStrategy strategy;

    public MatrixTask(int[][] a, int[][] b, int[][] result, int threadId, int nrThreads,
            DistributionStrategy strategy) {
        this.a = a;
        this.b = b;
        this.result = result;
        this.threadId = threadId;
        this.nrThreads = nrThreads;
        this.strategy = strategy;
    }

    @Override
    public void run() {
        strategy.compute(a, b, result, threadId, nrThreads);
    }
}
