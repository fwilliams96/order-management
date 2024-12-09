package com.fakecompany.order_management.auth.domain;

import com.fakecompany.order_management.shared.domain.BaseException;

public class UserAlreadyExistsError extends BaseException {

    public static final String CODE = "USR001";
    public static final String MESSAGE = "User with email %s already exists.";

    public UserAlreadyExistsError(String email) {
        super(String.format(MESSAGE, email));
        addError(CODE, String.format(MESSAGE, email));
    }
}
