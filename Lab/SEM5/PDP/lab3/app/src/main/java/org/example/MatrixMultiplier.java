package org.example;

public class MatrixMultiplier {
    public static void computeElement(int[][] a, int[][] b, int[][] result, int row, int col, int threadId) {
        int sum = 0;

        for(int i = 0; i < b.length; i++) {
            sum += a[row][i] * b[i][col];
        }

        result [row][col] = sum;
        System.out.printf("Thread %d computes (%d, %d) \n", threadId, row, col);
    }
}
