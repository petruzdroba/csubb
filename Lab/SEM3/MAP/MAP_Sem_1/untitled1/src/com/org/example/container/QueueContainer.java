package com.org.example.container;

import com.org.example.model.Task;

public class QueueContainer implements Container {

    private Task[] tasks;
    private int size;

    public QueueContainer(){
        this.tasks = new Task[10];
        this.size = 0;
    }

    @Override
    public Task remove() {
        return null;
    }

    @Override
    public void add(Task t) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }
}
