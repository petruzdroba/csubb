package org.example.domain;

public interface Observable {
    void notifyO(Order order);

    void addO(Observer o);

    void removeO(Observer o);
}
