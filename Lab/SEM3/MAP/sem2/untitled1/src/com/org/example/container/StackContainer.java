package com.org.example.container;

import com.org.example.model.Task;

public class StackContainer implements Container{

    private Task[] tasks;
    private int size;

    public StackContainer() {
        this.tasks = new Task[10];
        this.size = 0;
    }

    @Override
    public Task remove() {
        if(isEmpty()) return null;
        return tasks[--size];
    }

    @Override
    public void add(Task task) {
        if(tasks.length == size)
        {
            Task[] newTasks = new Task[tasks.length * 2];
            System.arraycopy(tasks, 0, newTasks, 0, tasks.length);
            tasks = newTasks;
        }
        tasks[size++] = task;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
