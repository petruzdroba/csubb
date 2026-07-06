package org.zdroba.rest;

import org.zdroba.exceptions.AlreadyExistsException;
import org.zdroba.exceptions.InvalidEngineException;
import org.zdroba.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> handleConflict(AlreadyExistsException e) {
        return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(InvalidEngineException.class)
    public ResponseEntity<?> handleBadRequest(InvalidEngineException e) {
        return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception e) {
        return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
    }
}