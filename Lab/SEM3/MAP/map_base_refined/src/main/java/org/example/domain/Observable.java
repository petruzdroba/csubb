package org.example.domain;

public interface Observable {
    void notifyO();

    void add(Observer o);
    void remove(Observer o);
}
