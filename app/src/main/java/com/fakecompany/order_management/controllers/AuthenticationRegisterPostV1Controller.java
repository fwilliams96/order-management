package com.fakecompany.order_management.controllers;

import com.fakecompany.order_management.api.AuthenticationRegisterPostV1Api;
import com.fakecompany.order_management.api.dto.JWTokenDto;
import com.fakecompany.order_management.api.dto.NewUserDto;
import com.fakecompany.order_management.auth.application.register_user.UserRegister;
import com.fakecompany.order_management.auth.domain.JWToken;
import com.fakecompany.order_management.auth.domain.NewUser;
import com.fakecompany.order_management.mappers.JwTokenToJwTokenDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationRegisterPostV1Controller implements AuthenticationRegisterPostV1Api {

    private final UserRegister userRegister;
    private final JwTokenToJwTokenDtoMapper jwTokenToJwTokenDtoMapper;

    @Override
    public ResponseEntity<JWTokenDto> postAuthenticationRegister(NewUserDto newUserDto) {
        JWToken register = userRegister.register(
                NewUser.builder()
                        .email(newUserDto.getEmail())
                        .password(newUserDto.getPassword())
                        .name(newUserDto.getName())
                        .build()
        );
        return ResponseEntity.ok(jwTokenToJwTokenDtoMapper.map(register));
    }

}
