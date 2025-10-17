package com.org.example.model;

import java.time.LocalDateTime;

import static com.org.example.utils.Constants.DATE_TIME_FORMATTER;

public class Message {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    private String id;
    private String subject;
    private String body;
    private String from;
    private LocalDateTime dateTime;

    public Message(String id, String subject, String body, String from, LocalDateTime dateTime) {
        this.id = id;
        this.subject = subject;
        this.body = body;
        this.from = from;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", from='" + from + '\'' +
                ", dateTime=" + dateTime.format(DATE_TIME_FORMATTER) +
                '}';
    }

}
