package com.org.example;

import com.org.example.factory.Strategy;
import com.org.example.model.Message;
import com.org.example.model.MessageTask;
import com.org.example.run.AbstractTaskRunner;
import com.org.example.run.DelayTaskRunner;
import com.org.example.run.PrinterTaskRunner;
import com.org.example.run.StrategyTaskRunner;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        var tasks = taskList();

        StrategyTaskRunner strategyTaskRunner = new StrategyTaskRunner(Strategy.LIFO);


        for (MessageTask task : tasks) {
            strategyTaskRunner.addTask(task);
        }

        //strategyTaskRunner.executeAll();

        //AbstractTaskRunner taskRunner = new PrinterTaskRunner(strategyTaskRunner);
        AbstractTaskRunner taskRunner = new DelayTaskRunner(strategyTaskRunner);

        taskRunner.executeAll();

    }

    private static MessageTask[] taskList()
    {


        Message mess1 = new Message("111", "Ab", "CD", "eu", LocalDateTime.now());
        MessageTask task1 = new MessageTask("111", "MHM", mess1);
        MessageTask task2 = new MessageTask("112", "MHM", mess1);
        MessageTask task3 = new MessageTask("144", "MHM", mess1);

        return new MessageTask[]{task1, task2, task3};

    }
}