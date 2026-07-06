package com.zdroba.mpp.controllers;

import com.zdroba.mpp.notification.WebSocketHandler;
import com.zdroba.mpp.service.IParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ParticipantController {
    private final IParticipantService service;
    private final WebSocketHandler ws;

    public ParticipantController(IParticipantService service, WebSocketHandler ws) {
        this.service = service;
        this.ws = ws;
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(
                service.get()
        );
    }

    @PostMapping("/ready")
    public ResponseEntity<?> ready(@RequestBody Map<String, Object> body){
        Long id = ((Number) body.get("id")).longValue();
        service.ready(id);
        ws.broadcast("USER UPDATE");

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public  ResponseEntity<?> score(@RequestBody Map<String, Object> body){
        Long id = ((Number) body.get("id")).longValue();
        Long judge = ((Number) body.get("judge")).longValue();
        Integer score = ((Number) body.get("score")).intValue();

        service.score(id, judge, score);
        ws.broadcast("USER UPDATE");

        return ResponseEntity.ok().build();
    }
}
