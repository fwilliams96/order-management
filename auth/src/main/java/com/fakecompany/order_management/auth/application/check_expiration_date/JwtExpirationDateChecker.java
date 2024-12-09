package com.fakecompany.order_management.auth.application.check_expiration_date;

import com.fakecompany.order_management.auth.application.find_claims.JwtClaimsFinder;
import com.fakecompany.order_management.auth.domain.JWToken;
import com.fakecompany.order_management.auth.domain.JwtClaims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class JwtExpirationDateChecker {

    private static final ZoneId MADRID_TIMEZONE = ZoneId.of("Europe/Madrid");

    private final JwtClaimsFinder jwtClaimsFinder;

    public boolean isExpired(JWToken token) {
        JwtClaims jwtClaims = jwtClaimsFinder.find(token);
        return jwtClaims.getExpiration().isBefore(LocalDateTime.now(MADRID_TIMEZONE));
    }

}
