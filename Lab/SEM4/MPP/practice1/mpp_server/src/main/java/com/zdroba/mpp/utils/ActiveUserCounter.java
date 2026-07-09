package com.zdroba.mpp.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class ActiveUserCounter {

    private static final AtomicInteger count = new AtomicInteger();

    public static void increment() {
        count.incrementAndGet();
    }

    public static void decrement() {
        count.decrementAndGet();
    }

    public static int getCount() {
        return count.get();
    }
}