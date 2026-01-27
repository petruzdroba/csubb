package org.example.service;

import org.example.domain.Observable;
import org.example.domain.Observer;
import org.example.domain.Order;
import org.example.domain.Status;

import java.util.List;

public interface OrderServiceI extends Observable, Observer {

    List<Order> getPage(int limit, int offset);

    List<Order> getPage(int limit, int offset, Status status, Long driverId);

    int pageCount(int pageSize,Status status, Long driverId);

    void add(String pickup, String destination, String client);

    void update(Long orderId, Long driverId);

    void markFinished(Long orderId);
}
