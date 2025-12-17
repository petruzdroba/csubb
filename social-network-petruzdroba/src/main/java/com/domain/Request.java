package com.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Request {
    public enum status{
        REJECTED, ACCEPTED, PENDING
    }

    private long id;
    private LocalDateTime data;
    private status status;
    private User from;
    private User to;

    public Request() {
    }

    public Request(User from, User to, status status, LocalDateTime now) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.data=now;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public status getStatus() {
        return status;
    }

    public void setStatus(status status) {
        this.status = status;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return from.getUsername() + "                           " + data.format(DateTimeFormatter.ofPattern(" HH':'mm dd MMM"));
    }
}
