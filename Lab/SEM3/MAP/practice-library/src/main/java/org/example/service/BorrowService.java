package org.example.service;

import org.example.domain.*;
import org.example.repo.BorrowRepo;

import java.util.ArrayList;
import java.util.List;

public class BorrowService implements Observable {
    private final BorrowRepo borrowRepo;
    private List<Observer> observers = new ArrayList<>();

    public BorrowService(BorrowRepo borrowRepo) {
        this.borrowRepo = borrowRepo;
    }

    public List<BorrowRequest> getAll(){
        return borrowRepo.getAll();
    }

    public List<BorrowRequest> getAll(Long patronId){
        return  borrowRepo.getAllByPatron(patronId);
    }

    public void add(Patron patron, List<Book> books){
        BorrowRequest req = new BorrowRequest(patron, books);
        borrowRepo.add(req);
        notifyO();
    }

    public void updateStatus(Long requestId, Status status){
        borrowRepo.updateStatus(requestId, status);
        notifyO();
    }

    @Override
    public void notifyO() {
        observers.forEach(Observer::update);
    }

    @Override
    public void add(Observer o) {
        if(!observers.contains(o))
            observers.add(o);
    }

    @Override
    public void remove(Observer o) {
        observers.remove(o);
    }
}
