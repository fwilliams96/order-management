package com.fakecompany.order_management.controllers;

import com.fakecompany.order_management.api.AuthenticationLoginPostV1Api;
import com.fakecompany.order_management.api.dto.JWTokenDto;
import com.fakecompany.order_management.api.dto.UserCredentialsDto;
import com.fakecompany.order_management.auth.application.login_user.UserLogin;
import com.fakecompany.order_management.auth.domain.JWToken;
import com.fakecompany.order_management.auth.domain.UserCredentials;
import com.fakecompany.order_management.mappers.JwTokenToJwTokenDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationLoginPostV1Controller implements AuthenticationLoginPostV1Api {

    private final UserLogin userLogin;
    private final JwTokenToJwTokenDtoMapper jwTokenToJwTokenDtoMapper;

    @Override
    public ResponseEntity<JWTokenDto> postAuthenticationLogin(UserCredentialsDto userCredentialsDto) {
        JWToken login = userLogin.login(
                UserCredentials.builder()
                        .email(userCredentialsDto.getEmail())
                        .password(userCredentialsDto.getPassword())
                        .build()
        );
        return ResponseEntity.ok(jwTokenToJwTokenDtoMapper.map(login));
    }
}
