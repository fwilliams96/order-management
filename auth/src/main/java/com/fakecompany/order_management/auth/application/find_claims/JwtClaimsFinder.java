package com.fakecompany.order_management.auth.application.find_claims;

import com.fakecompany.order_management.auth.domain.JWToken;
import com.fakecompany.order_management.auth.domain.JwtClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtClaimsFinder {

    private static final ZoneId MADRID_TIMEZONE = ZoneId.of("Europe/Madrid");

    @Value("${jwt.secret}")
    private final String jwtSecret;

    public JwtClaims find(JWToken token) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(this.jwtSecret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token.getAccessToken())
                .getBody();

        return JwtClaims.builder()
                .id(body.getId())
                .issuer(body.getIssuer())
                .subject(body.getSubject())
                .audience(body.getAudience())
                .expiration(dateToLocalDateTime(body.getExpiration()))
                .issuedAt(dateToLocalDateTime(body.getIssuedAt()))
                .notBefore(dateToLocalDateTime(body.getNotBefore()))
                .build();
    }

    public LocalDateTime dateToLocalDateTime(Date dateToConvert) {
        if (dateToConvert == null) return null;
        return dateToConvert.toInstant()
                .atZone(MADRID_TIMEZONE)
                .toLocalDateTime();
    }

}
