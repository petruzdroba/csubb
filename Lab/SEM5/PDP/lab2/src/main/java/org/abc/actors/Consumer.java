package org.abc.actors;

import org.abc.sd.MyQueueImpl;

import java.util.Optional;

public class Consumer implements Runnable{
    private final MyQueueImpl<Long> queue;
    private long sum = 0;

    public Consumer(MyQueueImpl<Long> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Optional<Long> product;

            while ((product = queue.dequeue()).isPresent())
                sum += product.get();
        } catch (InterruptedException e) {
            System.err.println("Interuppted exception " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public long getSum() {
        return sum;
    }
}
