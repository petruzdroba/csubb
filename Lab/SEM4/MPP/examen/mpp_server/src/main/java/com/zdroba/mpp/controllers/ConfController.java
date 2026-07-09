package com.zdroba.mpp.controllers;

import com.zdroba.mpp.entity.Configuration;
import com.zdroba.mpp.repository.ConfRepo;
import com.zdroba.mpp.service.ConfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfController {

    private final ConfService service;

    public ConfController(ConfService service) {
        this.service = service;
    }

    @GetMapping
    ResponseEntity<List<Configuration>> all() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    ResponseEntity<Configuration> add(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<String> letters = (List<String>) body.get("letters");
        String word = (String) body.get("word");

        return ResponseEntity.ok(service.add(letters, word));
    }
}
