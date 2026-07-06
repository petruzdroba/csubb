package com.zdroba.mpp.controllers;

import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.service.IGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {
    private final IGameService service;

    public GameController(IGameService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Game> get() {
        return ResponseEntity.ok(service.get());
    }

    @PostMapping("/start")
    public ResponseEntity<Game> start() {
        return ResponseEntity.ok(service.start());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Map<String, Object> body) {
        service.addPlayer(((Number) body.get("userId")).longValue(), (String) body.get("word"));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/guess")
    public ResponseEntity<Integer> guess(@RequestBody Map<String, Object> body) {
        Long userId = ((Number) body.get("userId")).longValue();
        Long wordId = ((Number) body.get("wordId")).longValue();
        char letter = ((String) body.get("letter")).charAt(0);

        return ResponseEntity.ok(service.guess(userId, wordId, letter));
    }

    @GetMapping("/{gameId}/player/{userId}/guesses")
    public ResponseEntity<?> getPlayerGuesses(@PathVariable Long gameId, @PathVariable Long userId) {
        return ResponseEntity.ok(service.getPlayerGuesses(gameId, userId));
    }
}
