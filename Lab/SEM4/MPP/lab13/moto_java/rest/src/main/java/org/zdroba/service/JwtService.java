package org.zdroba.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService implements IJwtService {

    private final SecretKey secretKey;
    private static final long EXPIRATION_MS = 1000L * 60 * 60 * 24; // 24 hours

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public SecretKey getSecretKey() {
        return secretKey;
    }

    @Override
    public String generateToken(Long userId) {
        return Jwts.builder()
                .claim("id", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public Long extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Long.class);
    }
}