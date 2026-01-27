package org.example.service;

import org.example.domain.Table;
import org.example.repo.TableRepo;

import java.util.List;

public class TableService {

    private final TableRepo tableRepo;

    public TableService(TableRepo tableRepo) {
        this.tableRepo = tableRepo;
    }

    public List<Table> getAll(){
        return tableRepo.getAll();
    }
}
