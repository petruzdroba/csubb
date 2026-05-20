package org.zdroba.sync;

public interface Observable {
    void add(Observer observer);

    void remove(Observer observer);

    void notify(String message);
}
