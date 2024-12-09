package com.fakecompany.order_management.auth.application.generate_jwt;

import com.fakecompany.order_management.auth.domain.JWToken;
import com.fakecompany.order_management.auth.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtGenerator {

    @Value("${jwt.secret}")
    private final String jwtSecret;

    public JWToken generate(User user) {
        Instant now = Instant.now();
        Instant expirationInstant = now.plus(12, ChronoUnit.HOURS); //12 hours of duration
        String token = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getId().toString())
                .claim("name", user.getName())
                .claim("roles", user.getRoles()) // Reclamo de los roles
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationInstant))
                .signWith(SignatureAlgorithm.HS512, this.jwtSecret.getBytes(StandardCharsets.UTF_8))
                .compact();

        return JWToken.builder()
                .accessToken(token)
                .build();
    }

}
