package com.zdroba.mpp.service;

import com.zdroba.mpp.entity.Configuration;
import com.zdroba.mpp.repository.ConfRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfService {

    private final ConfRepo repo;


    public ConfService(ConfRepo repo) {
        this.repo = repo;
    }

    public Configuration add(List<String> letters, String word) {
        letters.forEach(l -> {
            if(!word.contains(l))
                throw new RuntimeException("Word wrong");
        });

        if(letters.size() != word.length())
            throw new RuntimeException("Word size wrong");

        Configuration c = new Configuration();
        c.letters = letters;
        c.word = word;

        return repo.save(c);
    }

    public List<Configuration> getAll(){
        return repo.findAll();
    }
}
