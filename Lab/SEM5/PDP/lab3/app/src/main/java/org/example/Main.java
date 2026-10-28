package org.example;

import java.util.Random;

import org.example.strategy.ColumnMajorBlockStrategy;
import org.example.strategy.DistributionStrategy;
import org.example.strategy.RowMajorBlockStrategy;
import org.example.strategy.CyclicStrategy;


@SuppressWarnings("unused")
public class Main {
    public static void main(String[] args) throws InterruptedException {
        int rows = args.length > 0 ? Integer.parseInt(args[0]) : 9;
        int cols = args.length > 1 ? Integer.parseInt(args[1]) : 9;
        int nrThreads = args.length > 2 ? Integer.parseInt(args[2]) : 4;

        Random random = new Random();
        int[][] a = new int[rows][cols];
        int[][] b = new int[cols][rows];
        int[][] result = new int[rows][rows];

        for (int[] row : a)
            for (int j = 0; j < row.length; j++)
                row[j] = random.nextInt(10);

        for (int[] row : b)
            for (int j = 0; j < row.length; j++)
                row[j] = random.nextInt(10);

        Thread[] threads = new Thread[nrThreads];
        DistributionStrategy strategy = new CyclicStrategy();

        long start = System.nanoTime();

        for (int i = 0; i < nrThreads; i++) {
            threads[i] = new Thread(
                new MatrixTask(a, b, result, i, nrThreads, strategy)
            );
            threads[i].start();
        }

        for (Thread thread : threads)
            thread.join();

        System.out.printf("Matrix: %dx%d, Threads: %d, Time: %.3f ms%n",
                rows, cols, nrThreads, (System.nanoTime() - start) / 1_000_000.0);
    }
}