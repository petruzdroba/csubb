package org.abc;

import org.abc.sd.MyQueueImpl;
import org.abc.actors.Consumer;
import org.abc.actors.Producer;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int size = args.length > 0 ? Integer.parseInt(args[0]) : 100_000;
        int capacity = args.length > 1 ? Integer.parseInt(args[1]) : 100;

        int[] a = new int[size];
        int[] b = new int[size];
        Random random = new Random(42);
        long expected = 0;

        for (int i = 0; i < size; i++) {
            a[i] = random.nextInt(100) + 1;
            b[i] = random.nextInt(100) + 1;
            expected += (long) a[i] * b[i];
        }

        MyQueueImpl<Long> queue = new MyQueueImpl<>(capacity);
        Consumer consumer = new Consumer(queue);

        Thread producerThread = new Thread(new Producer(a, b, queue), "producer");
        Thread consumerThread = new Thread(consumer, "consumer");

        long start = System.nanoTime();

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        long elapsed = System.nanoTime() - start;

        System.out.println("Vector size: " + size);
        System.out.println("Queue capacity: " + capacity);
        System.out.println("Scalar product: " + consumer.getSum());
        System.out.println("Expected: " + expected);
        System.out.println("Correct: " + (consumer.getSum() == expected));
        System.out.printf("Execution time: %.3f ms%n", elapsed / 1_000_000.0);
    }
}
