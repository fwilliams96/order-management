package com.fakecompany.order_management.orders.domain;

import com.fakecompany.order_management.shared.domain.BaseException;

import java.util.UUID;

public class OrderNotOpenError extends BaseException {

    public static final String ERROR_CODE = "ORD002";
    public static final String MESSAGE = "Order with id %s is not open";

    public OrderNotOpenError(UUID id) {
        super(String.format(MESSAGE, id));
        this.addError(ERROR_CODE, String.format(MESSAGE, id));
    }
}
