package com.fakecompany.order_management.mappers;

import com.fakecompany.order_management.auth.domain.JWToken;
import com.fakecompany.order_management.api.dto.JWTokenDto;
import org.springframework.stereotype.Component;

@Component
public class JwTokenToJwTokenDtoMapper {

    public JWTokenDto map(JWToken jwToken) {
        JWTokenDto jwTokenDto = new JWTokenDto();
        jwTokenDto.setAccessToken(jwToken.getAccessToken());
        return jwTokenDto;
    }

}
