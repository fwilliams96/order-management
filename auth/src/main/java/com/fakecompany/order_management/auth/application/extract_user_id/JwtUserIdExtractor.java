package com.fakecompany.order_management.auth.application.extract_user_id;

import com.fakecompany.order_management.auth.application.find_claims.JwtClaimsFinder;
import com.fakecompany.order_management.auth.domain.JWToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtUserIdExtractor {

    private final JwtClaimsFinder jwtClaimsFinder;

    public UUID extract(JWToken token) {
        String userId = jwtClaimsFinder.find(token).getSubject();
        return UUID.fromString(userId);
    }

}
