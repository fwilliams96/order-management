package com.fakecompany.order_management.products.domain;

import com.fakecompany.order_management.shared.domain.BaseException;

import java.util.UUID;

public class ProductNotFoundError extends BaseException {

    public static final String ERROR_CODE = "PRD001";
    public static final String MESSAGE = "Product with id %s not found";

    public ProductNotFoundError(UUID id) {
        super(String.format(MESSAGE, id));
        this.addError(ERROR_CODE, String.format(MESSAGE, id));
    }
}
