package com.fakecompany.order_management.orders.domain;

import com.fakecompany.order_management.shared.domain.BaseException;

import java.util.UUID;

public class OrderNotFoundError extends BaseException {

    public static final String ERROR_CODE = "ORD001";
    public static final String MESSAGE = "Order with id %s not found";

    public OrderNotFoundError(UUID id) {
        super(String.format(MESSAGE, id));
        this.addError(ERROR_CODE, String.format(MESSAGE, id));
    }
}
