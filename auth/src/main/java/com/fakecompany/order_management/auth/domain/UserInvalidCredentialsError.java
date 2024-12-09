package com.fakecompany.order_management.auth.domain;

import com.fakecompany.order_management.shared.domain.BaseException;

public class UserInvalidCredentialsError extends BaseException {

    public static final String CODE = "USR002";
    public static final String MESSAGE = "User credentials not valid.";

    public UserInvalidCredentialsError() {
        super(MESSAGE);
        addError(CODE, MESSAGE);
    }
}
