package org.zdroba.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.InvalidPasswordException;
import org.zdroba.exceptions.NotFoundException;
import org.zdroba.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) throws InvalidPasswordException, NotFoundException {
        String token = authService.login(body.get("email"), body.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) throws AlreadyExistsException {
        String token = authService.register(body.get("email"), body.get("password"));
        return ResponseEntity.status(201).body(Map.of("token", token));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }
}
