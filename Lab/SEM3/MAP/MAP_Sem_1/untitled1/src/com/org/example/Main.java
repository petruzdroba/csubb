package com.org.example;

import com.org.example.model.Message;
import com.org.example.model.MessageTask;

import java.time.LocalDateTime;

public class Main {

    static void main() {

        Message mess1 = new Message("111", "Ab", "CD", "eu", LocalDateTime.now());
        MessageTask task1 = new MessageTask("111", "MHM", mess1);
        MessageTask task2 = new MessageTask("112", "MHM", mess1);
        MessageTask task3 = new MessageTask("144", "MHM", mess1);
        task1.execute();
        task2.execute();
        task3.execute();

    }
}