package com.fakecompany.order_management.auth.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class NewUser extends UserCredentials {

    private String name;
    private String email;
    private String password;

}
