package com.fakecompany.order_management.auth.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class JwtClaims {

    private String issuer;
    private String subject;
    private String audience;
    private LocalDateTime expiration;
    private LocalDateTime notBefore;
    private LocalDateTime issuedAt;
    private String id;

}
