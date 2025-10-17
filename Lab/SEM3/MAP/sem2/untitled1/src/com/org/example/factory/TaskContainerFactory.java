package com.org.example.factory;

import com.org.example.container.Container;
import com.org.example.container.QueueContainer;
import com.org.example.container.StackContainer;

public class TaskContainerFactory implements Factory{

    private static TaskContainerFactory INSTANCE;

    private TaskContainerFactory() {
    }

    public static TaskContainerFactory getInstance() {
        if(INSTANCE == null)
        {
            INSTANCE = new TaskContainerFactory();
        }
        return INSTANCE;
    }


    @Override
    public Container container(Strategy strategy) {
        return switch (strategy) {
            case FIFO -> new QueueContainer();
            case LIFO -> new StackContainer();
        };
    }
}
