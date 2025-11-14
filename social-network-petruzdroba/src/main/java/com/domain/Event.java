package com.domain;

import com.exceptions.DomainException;

import java.util.ArrayList;
import java.util.List;

public abstract class Event {
    protected List<User> subscribers;
    protected long id;

    public Event(long id) {
        this.subscribers = new ArrayList<User>();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<User> subscribers) {
        this.subscribers = subscribers;
    }

    public abstract void start();

    /**
     * Subscribes a user to this event.
     *
     * @param u the user to subscribe
     * @throws DomainException if the user is already subscribed
     */
    public void subscribe(User u) throws DomainException{
        if(subscribers.contains(u))
            throw new DomainException("User already subscribed \n");
        subscribers.add(u);
    }

    /**
     * Unsubscribes a user from this event.
     *
     * @param u the user to unsubscribe
     * @throws DomainException if the user is not currently subscribed
     */
    public void unsubscribe(User u) throws DomainException{
        if(!subscribers.contains(u))
            throw new DomainException("User isnt subscribed \n");
        subscribers.remove(u);
    }

    public String notification(){
        return "notified";
    }

    /**
     * Notifies all subscribed users by calling their notify(Event) method.
     * @see User#notify(Event) 
     */
    public void notifySubscribers(){
        for(User u: subscribers){
            u.notify(this);
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                "subscribers=" + subscribers +
                ", id=" + id +
                '}';
    }
}
