package com.zdroba.mpp.controllers;


import com.zdroba.mpp.entity.Configuration;
import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.service.IGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {

    private final IGameService service;

    public GameController(IGameService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    ResponseEntity<Game> get(@PathVariable Long id){
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping("/add")
    ResponseEntity<Game> add(@RequestBody Map<String, Object> body) {
        Long userId = ((Number) body.get("userId")).longValue();

        return ResponseEntity.ok(service.addPlayer(userId));
    }

    @PostMapping("/start")
    ResponseEntity<Game> start(@RequestBody Map<String, Object> body){
        Long gameId = ((Number) body.get("gameId")).longValue();

        return ResponseEntity.ok(service.start(gameId));
    }

    @PostMapping("/action")
    ResponseEntity<Integer> action(@RequestBody Map<String, Object> body) {
        Long gameId = ((Number) body.get("gameId")).longValue();
        Long userId = ((Number) body.get("userId")).longValue();
        String word = (String) body.get("word").toString();

        return ResponseEntity.ok(service.action(userId, gameId, word));
    }

    @PostMapping("/config")
    void addC(@RequestBody Map<String, Object> body) {
        Long gameId = ((Number) body.get("gameId")).longValue();
        Long confId = ((Number) body.get("confId")).longValue();

        service.addConfig(gameId, confId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId, @RequestParam(required=false) Integer Y){
        return ResponseEntity.ok(service.getByUserAndY(userId, Y));
    }
}
