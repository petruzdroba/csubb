package org.example.service;

import org.example.domain.Observable;
import org.example.domain.Observer;
import org.example.domain.Order;
import org.example.repo.OrderRepository;
import org.example.domain.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService implements OrderServiceI {

    private final OrderRepository orderRepository;
    private List<Observer> observers = new ArrayList<Observer>();

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getPage(int limit, int offset) {
        return List.of();
    }

    @Override
    public List<Order> getPage(int limit, int offset, Status status, Long driverId) {
        return orderRepository.getPageFiltered(limit, offset, status, driverId);
    }

    @Override
    public int pageCount(int pageSize, Status status, Long driverId) {
        return orderRepository.pageCountFiltered(pageSize, status, driverId);
    }

    @Override
    public void add(String pickup, String destination, String client) {
        Order order = new Order(pickup, destination, client);
        orderRepository.add(null, order);
        notifyO(order);
    }

    @Override
    public void update(Long orderId, Long driverId) {
        orderRepository.updateDriver(orderRepository.find(orderId), driverId);
        notifyO();
    }

    @Override
    public void markFinished(Long orderId) {
        Order newOrder = orderRepository.find(orderId);
        newOrder.setStatus(Status.FINISHED);
        newOrder.setEndDate(LocalDateTime.now());
        orderRepository.update(orderId, newOrder);
        notifyO();
    }

    @Override
    public void notifyO(Order order) {
        if (order == null || observers.isEmpty()) return;

        Observer first = observers.removeFirst();
        first.update(order);
        observers.add(first);
    }

    public void notifyO(){
        observers.forEach(o -> o.update(null));
    }

    @Override
    public void addO(Observer o) {
        if (!observers.contains(o))
            observers.add(o);
    }

    @Override
    public void removeO(Observer o) {
        observers.remove(o);
    }

    @Override
    public void update(Order order) {
        if(order != null){
            notifyO(order);
        }
    }
}
