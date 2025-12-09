package com.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private  long id;
    private  User from;
    private List<User> to;
    private String message;
    private LocalDateTime data;
    private  Message reply;

    //Constructor mesaj nou
    public Message(long id, User from, String message, LocalDateTime data) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.data = data;
        this.to = new ArrayList<User>();
        this.reply = null; //mesaj nou
    }

    //Constructor Reply
    public Message(long id, User from, String message, LocalDateTime data, Message reply, List<User> to) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.data = data;
        this.reply = reply;
        this.to = to;
    }

    public long getId() {
        return id;
    }

    public User getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Message getReply() {
        return reply;
    }

    public List<User> getTo() {
        return to;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }
}
