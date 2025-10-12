package com.org.example.container;

import com.org.example.container.Container;
import com.org.example.model.Task;

public class StackContainer implements Container{
    private Task[] tasks;
    private int size;

    public StackContainer() {
        this.tasks = new Task[10];
        this.size = 0;
    }

    public Task remove(){
        if(isEmpty())
            return null;
        return tasks[--size];
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size(){
        return this.size;
    }

    public void add(Task t){
        this.tasks[size++] = t;
    }
}