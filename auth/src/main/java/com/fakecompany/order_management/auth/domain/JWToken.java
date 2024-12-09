package com.fakecompany.order_management.auth.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JWToken {

    private String accessToken;

}
