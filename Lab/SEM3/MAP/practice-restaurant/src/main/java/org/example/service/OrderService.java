package org.example.service;

import org.example.domain.MenuItem;
import org.example.domain.Observable;
import org.example.domain.Observer;
import org.example.domain.Order;
import org.example.repo.OrderRepo;

import java.util.ArrayList;
import java.util.List;

public class OrderService implements Observable {

    private final OrderRepo orderRepo;
    private List<Observer> observers = new ArrayList<>();

    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<Order> getAll(){
        return orderRepo.getAll();
    }

    public void add(long tableId, List<MenuItem> items){
        Order order = new Order(tableId, items);
        orderRepo.add(order);

        notifyO();
    }

    @Override
    public void add(Observer o) {
        if(!observers.contains(o))
            observers.add(o);
    }

    @Override
    public void notifyO() {
        observers.forEach(Observer::update);
    }
}
