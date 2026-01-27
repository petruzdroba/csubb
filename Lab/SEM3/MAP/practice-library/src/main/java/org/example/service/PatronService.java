package org.example.service;

import org.example.domain.Patron;
import org.example.repo.PatronRepo;

import java.util.List;

public class PatronService {
    private final PatronRepo patronRepo;


    public PatronService(PatronRepo patronRepo) {
        this.patronRepo = patronRepo;
    }

    public List<Patron> getAll(){
        return patronRepo.getAll();
    }
}
