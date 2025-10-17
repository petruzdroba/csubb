package com.org.example.run;

import java.util.concurrent.TimeUnit;

public class DelayTaskRunner extends AbstractTaskRunner{
    public DelayTaskRunner(TaskRunner runner) {
        super(runner);
    }
    @Override
    public void executeOneTask() {
        try {

            //Thread.sleep(3000);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        runner.executeOneTask();
    }
}
