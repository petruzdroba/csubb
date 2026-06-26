package com.zdroba.mpp.controllers;

import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.service.IGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

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

    @GetMapping("/{id}/start")
    ResponseEntity<Game> start(@PathVariable Long id){
        return ResponseEntity.ok(service.start(id));
    }

    @GetMapping("/user/{id}")
    ResponseEntity<List<Game>> getGame(@PathVariable Long id){
        return ResponseEntity.ok(service.getGames(id));
    }

    @PostMapping
    ResponseEntity<Game> addPlayer(@RequestBody Map<String, Object> body){
        Long userId = ((Number) body.get("userId")).longValue();
        int x_1 = ((Number) body.get("X1")).intValue();
        int x_2 = ((Number) body.get("X2")).intValue();
        int y_1 = ((Number) body.get("Y1")).intValue();
        int y_2 = ((Number) body.get("Y2")).intValue();

        return ResponseEntity.ok(service.addPlayer(userId, x_1, y_1, x_2, y_2));
    }

    @PutMapping
    ResponseEntity<Boolean> addGuess(@RequestBody Map<String, Object> body){
        Long userId = ((Number) body.get("userId")).longValue();
        Long gameId = ((Number) body.get("gameId")).longValue();
        int x = ((Number) body.get("X")).intValue();
        int y = ((Number) body.get("Y")).intValue();


        return ResponseEntity.ok(service.addGuess(userId,gameId, x, y));
    }
}
