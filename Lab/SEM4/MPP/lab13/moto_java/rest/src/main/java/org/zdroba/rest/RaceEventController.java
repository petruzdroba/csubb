package org.zdroba.rest;

import org.zdroba.entity.RaceEvent;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.InvalidEngineException;
import org.zdroba.exceptions.NotFoundException;
import org.zdroba.notification.RaceWebSocketHandler;
import org.zdroba.service.RaceEventRestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/races")
@CrossOrigin(origins = "*", methods = {
        RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE,
        RequestMethod.OPTIONS
})
public class RaceEventController {

    private final RaceEventRestService service;
    private final RaceWebSocketHandler webSocketHandler;

    public RaceEventController(RaceEventRestService service, RaceWebSocketHandler webSocketHandler) {
        this.service = service;
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) Integer engine) throws NotFoundException {
        if (engine != null) {
            return ResponseEntity.ok(service.filter(engine));
        }
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaceEvent> getById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<RaceEvent> create(@RequestBody Map<String, Object> body) throws AlreadyExistsException, InvalidEngineException {
        int engine = ((Number) body.get("engine")).intValue();
        RaceEvent created = service.save(engine);
        webSocketHandler.broadcast("{\"type\":\"CREATED\",\"id\":" + created.getId() + "}");
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RaceEvent> update(@PathVariable Long id, @RequestBody Map<String, Object> body) throws AlreadyExistsException, NotFoundException, InvalidEngineException {
        int engine = ((Number) body.get("engine")).intValue();
        RaceEvent updated = service.update(id, engine);
        webSocketHandler.broadcast("{\"type\":\"UPDATED\",\"id\":" + id + "}");
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws NotFoundException {
        service.delete(id);
        webSocketHandler.broadcast("{\"type\":\"DELETED\",\"id\":" + id + "}");
        return ResponseEntity.noContent().build();
    }
}