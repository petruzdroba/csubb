package com.org.example.run;

import com.org.example.container.Container;
import com.org.example.factory.Strategy;
import com.org.example.factory.TaskContainerFactory;
import com.org.example.model.Task;

public class StrategyTaskRunner implements TaskRunner{

    private Container container;


    public StrategyTaskRunner(Strategy strategy) {
        this.container = TaskContainerFactory.getInstance().container(strategy);
    }

    @Override
    public void executeOneTask() {
        if(!container.isEmpty())
        {
            Task task = container.remove();
            task.execute();
        }
    }

    @Override
    public void executeAll() {
        while(!container.isEmpty())
        {
            executeOneTask();
        }
    }

    @Override
    public void addTask(Task t) {
        container.add(t);
    }

    @Override
    public boolean hasTask() {
        return !container.isEmpty();
    }
}
