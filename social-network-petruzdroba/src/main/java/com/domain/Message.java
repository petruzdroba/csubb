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
    public Message(User from, String message, LocalDateTime data, List<User> to) {
        this.from = from;
        this.message = message;
        this.data = data;
        this.to = to;
        this.reply = null; //mesaj nou
    }

    //Constructor Reply
    public Message(User from, String message, LocalDateTime data, Message reply, List<User> to) {
        this.from = from;
        this.message = message;
        this.data = data;
        this.reply = reply;
        this.to = to;
    }

    public Message(long id, User fromUser, String messageText, LocalDateTime date, Message replyMessage, List<User> receivers) {
        this.id = id;
        this.from=fromUser;
        this.message=messageText;
        this.data=date;
        this.reply=replyMessage;
        this.to=receivers;
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
