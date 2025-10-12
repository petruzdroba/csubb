package com.org.example.container;

import com.org.example.model.Task;

public interface Container {
    public Task remove();

    public void add(Task t);

    public boolean isEmpty();

    public int size();
}
