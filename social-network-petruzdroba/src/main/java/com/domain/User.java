package com.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class User implements Observable, Observer{
    private final long id;
    private final String username;
    private final String email;
    private final String password;

    public User(long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Called when this user is notified about an event.
     *
     * @param e the event that triggered the notification
     * @see Event#notifySubscribers()
     */
    public void notify(Event e) {
        for(Observer o:observers){
            o.update(e.notification());
        }
    }

    public void notify(String message) {
        for(Observer o:observers){
            o.update(message);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getPassword() {
        return password;
    }

    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer o) {
        if (!observers.contains(o)) observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() { //notifies controller that it has gotten a message
        for (Observer o : observers) {
            o.update();
        }
    }

    @Override
    public void update() { //when user gets a message, it notifies controller
        notifyObservers();
    }

    @Override
    public void update(String message) {
        notify(message);
    }
}