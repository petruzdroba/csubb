package com.domain;

public interface Observer {
    void update();

    void update(String message);
}
