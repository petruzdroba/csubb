package org.zdroba.service;

import javax.crypto.SecretKey;

public interface IJwtService {
    SecretKey getSecretKey();
    String generateToken(Long userId);
    Long extractUserId(String token);
}