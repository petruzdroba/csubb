package org.example.service;

import org.example.domain.Meci;
import org.example.repo.MeciRepo;

import java.util.List;

public class MeciService {
    private final MeciRepo meciRepo;

    public MeciService(MeciRepo meciRepo) {
        this.meciRepo = meciRepo;
    }

    public List<Meci> get(){
        return meciRepo.getAll();
    }
}
