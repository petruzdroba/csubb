package com.zdroba.mpp.controllers;

import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.entity.Guess;
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

    @GetMapping("/{id}")
    ResponseEntity<Game> get(@PathVariable Long id){
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping("/start")
    ResponseEntity<Game> start(@RequestBody Map<String, Object> body){
        Long id= ((Number) body.get("userId")).longValue();

        return ResponseEntity.ok(service.start(id));
    }

    @PutMapping("/guess")
     ResponseEntity<Guess> add(@RequestBody Map<String, Object> body){
        Long userId= ((Number) body.get("userId")).longValue();
        Long gameId= ((Number) body.get("gameId")).longValue();
        int x= ((Number) body.get("X")).intValue();
        int y= ((Number) body.get("Y")).intValue();

        return ResponseEntity.ok(service.guess(userId, gameId, x,y));
    }
}
