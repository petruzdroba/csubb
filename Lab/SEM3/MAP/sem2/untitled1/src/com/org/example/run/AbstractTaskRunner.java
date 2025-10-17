package com.org.example.run;

import com.org.example.model.Task;

public abstract class AbstractTaskRunner implements TaskRunner{

    protected TaskRunner runner;

    protected AbstractTaskRunner(TaskRunner runner) {
        this.runner = runner;
    }

    @Override
    public void executeOneTask() {
        runner.executeOneTask();
    }

    @Override
    public void executeAll() {
        while(runner.hasTask())
        {
            executeOneTask();
        }
    }

    @Override
    public void addTask(Task t) {
        runner.addTask(t);
    }

    @Override
    public boolean hasTask() {
        return runner.hasTask();
    }
}
