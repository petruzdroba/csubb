package com.org.example.model;


import com.org.example.utils.Constants;

import java.time.LocalDateTime;

import static com.org.example.utils.Constants.DATE_TIME_FORMATTER;

public class MessageTask extends Task {


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    private Message message;

    public MessageTask(String taskID, String description, Message message) {
        super(taskID, description);
        this.message = message;

    }


    @Override
    public void execute() {
        System.out.println(this);
    }

    @Override
    public String toString() {

        return super.toString() + " " + this.message;
    }
}
