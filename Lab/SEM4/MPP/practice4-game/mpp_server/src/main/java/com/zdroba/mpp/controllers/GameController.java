package com.zdroba.mpp.controllers;

import com.zdroba.mpp.entity.Game;
import com.zdroba.mpp.entity.Points;
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

    @GetMapping
    ResponseEntity<Game> get(){
        return ResponseEntity.ok(
                service.get()
        );
    }

    @PostMapping("/add")
    ResponseEntity<?> addPlayer(@RequestBody Map<String, Object> body){
        Long userId = ((Number) body.get("userId")).longValue();
        service.addPlayer(userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/start")
    ResponseEntity<?> start(@RequestBody Map<String, Object> body){
        return ResponseEntity.ok(service.start());
    }

    @PostMapping("/tom")
    ResponseEntity<Integer> tom(@RequestBody Map<String, Object> body){
        Long userId = ((Number) body.get("userId")).longValue();
        String tara = ((String) body.get("tara"));
        String oras = ((String) body.get("oras"));
        String mare = ((String) body.get("mare"));

        return ResponseEntity.ok(service.addTom(userId, tara, oras, mare));
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Points>> summary(@PathVariable Long id){
        return ResponseEntity.ok(
                service.summary(id)
        );
    }

    @GetMapping("/{gameId}/player/{userId}")
    public ResponseEntity<?> something(@PathVariable Long gameId, @PathVariable Long userId) {
        return ResponseEntity.ok(service.summary(gameId, userId));
    }
}
