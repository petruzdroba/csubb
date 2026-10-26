package org.abc.sd;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueueImpl<T> implements MyQueue<T> {

    private final ArrayDeque<T> queue = new ArrayDeque<>();
    private final int capacity;
    private final ReentrantLock mutex = new ReentrantLock();
    private final Condition cv = mutex.newCondition();
    private boolean closed = false;


    public MyQueueImpl(int capacity) throws IllegalArgumentException {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity cannot be ngative");
        this.capacity = capacity;
    }

    @Override
    public void enqueue(T value) throws InterruptedException {
        mutex.lock();
        try {
            while (queue.size() == capacity && !closed) {
                cv.await();
            }

            if (closed) throw new IllegalStateException("Queue is closed");

            queue.add(value);
            cv.signalAll();
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public Optional<T> dequeue() throws InterruptedException {
        mutex.lock();
        try {
            while (queue.isEmpty() && !closed) {
                cv.await();
            }

            if (queue.isEmpty()) return Optional.empty();

            T value = queue.remove();
            cv.signalAll();
            return Optional.of(value);
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void close() {
        mutex.lock();
        try {
            closed = true;
            cv.signalAll();
        } finally {
            mutex.unlock();
        }
    }
}
