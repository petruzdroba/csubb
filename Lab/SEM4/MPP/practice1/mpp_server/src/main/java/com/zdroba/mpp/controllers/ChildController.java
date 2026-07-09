package com.zdroba.mpp.controllers;

import com.zdroba.mpp.entity.Child;
import com.zdroba.mpp.exceptions.NotFoundException;
import com.zdroba.mpp.notification.WebSocketHandler;
import com.zdroba.mpp.service.IChildService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/child")
public class ChildController {

    private final IChildService service;
    private final WebSocketHandler handler;

    public ChildController(IChildService service, WebSocketHandler handler) {
        this.service = service;
        this.handler = handler;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll( @RequestParam(required = false) Long checkpointId) {
        if(checkpointId != null){
            return ResponseEntity.ok(
                    service.getCheck(checkpointId)
            );
        }

        return ResponseEntity.ok(
                service.get()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Child> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Child> update(@PathVariable Long id, @RequestBody Map<String, Object> body) throws NotFoundException {
        Instant time = Instant.parse((String) body.get("time"));
        Long checkpointId = ((Number) body.get("checkpointId")).longValue();

        Child child = service.update(id, checkpointId, time);

        handler.broadcast("CHILD_UPDATE");

        return ResponseEntity.ok(child);
    }
}
