package org.abc.sd;

import java.util.Optional;

public interface MyQueue<T> {
    void enqueue(T value) throws InterruptedException;

    Optional<T> dequeue() throws InterruptedException;

    void close();
}
