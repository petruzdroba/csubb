package com.zdroba.mpp.controllers;

import com.zdroba.mpp.entity.User;
import com.zdroba.mpp.exceptions.AlreadyExistsException;
import com.zdroba.mpp.exceptions.InvalidPasswordException;
import com.zdroba.mpp.exceptions.NoSessionException;
import com.zdroba.mpp.exceptions.NotFoundException;
import com.zdroba.mpp.notification.WebSocketHandler;
import com.zdroba.mpp.service.AuthService;
import com.zdroba.mpp.utils.ActiveUserCounter;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final WebSocketHandler ws;

    public AuthController(AuthService authService, WebSocketHandler ws, WebSocketHandler handler) {
        this.authService = authService;
        this.ws = ws;
        this.handler = handler;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> body,
            HttpSession session) throws NotFoundException, InvalidPasswordException {

        String email = body.get("email");
        String password = body.get("password");

        User user = authService.login(email, password);

        session.setAttribute("user", user);
        session.setAttribute("points", 0);
        ActiveUserCounter.increment();

        return ResponseEntity.ok(
                Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "points", session.getAttribute("points")
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody Map<String, String> body,
            HttpSession session) throws AlreadyExistsException {

        User user = authService.register(
                body.get("email"),
                body.get("name"),
                body.get("password")
        );

        session.setAttribute("user", user);
        session.setAttribute("points", 0);
        ActiveUserCounter.increment();

        return ResponseEntity.status(201).body(
                Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "points", session.getAttribute("points")
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        ActiveUserCounter.decrement();

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) throws NoSessionException {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            throw new NoSessionException("No session found.");
        }

        return ResponseEntity.ok(
                Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "points", session.getAttribute("points")
                )
        );
    }

    private final WebSocketHandler handler;

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        handler.sendToUser(1L, "Regele");

        handler.sendToUsers(List.of(1L, 2L), "Regii");

        handler.broadcast("Regii3");

        return ResponseEntity.ok().build();
    }
}
