package org.abc.actors;

import org.abc.sd.MyQueueImpl;

public class Producer implements Runnable {
    private final int[] a;
    private final int[] b;
    private final MyQueueImpl<Long> queue;

    public Producer(int[] a, int[] b, MyQueueImpl<Long> queue) {
        if (a.length != b.length) throw new IllegalArgumentException("Vectors must be same size");

        this.a = a;
        this.b = b;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for ( int i = 0; i<a.length; i++) {
                queue.enqueue((long) a[i] * b[i]);
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted exception "+ e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            queue.close();
        }
    }
}
