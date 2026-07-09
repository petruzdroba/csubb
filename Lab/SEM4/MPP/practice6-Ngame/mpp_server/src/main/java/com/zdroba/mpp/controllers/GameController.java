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

        return ResponseEntity.ok(service.action(userId, gameId));
    }

    @PostMapping("/configuration")
    ResponseEntity<Void> setConfiguration(@RequestBody Map<String, Object> body) {
        Long gameId = ((Number) body.get("gameId")).longValue();
        Long configurationId = ((Number) body.get("configurationId")).longValue();

        service.setConfiguration(gameId, configurationId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{gameId}/configuration")
    ResponseEntity<Configuration> getConfiguration(@PathVariable Long gameId) {
        return ResponseEntity.ok(service.getConfiguration(gameId));
    }

    @PostMapping("/configuration/add")
    ResponseEntity<Configuration> addConfiguration(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Integer> board = (List<Integer>) body.get("board");

        return ResponseEntity.ok(service.addConfiguration(board));
    }

    @GetMapping("/configuration")
    ResponseEntity<List<Configuration>> getConfigurations() {
        return ResponseEntity.ok(service.getConfigurations());
    }

    @GetMapping("/won-games/{userId}")
    ResponseEntity<List<Game>> wonGames(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getGamesWithAtLeastPoints(userId, 5));
    }
}
