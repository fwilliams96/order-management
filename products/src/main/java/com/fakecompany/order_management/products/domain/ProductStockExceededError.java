package com.fakecompany.order_management.products.domain;

import com.fakecompany.order_management.shared.domain.BaseException;

import java.util.UUID;

public class ProductStockExceededError extends BaseException {

    public static final String ERROR_CODE_PRODUCT = "PRD001_PRODUCT_%s";
    public static final String ERROR_CODE_CURRENT = "PRD001_CURRENT_%s";
    public static final String ERROR_CODE_REQUIRED = "PRD001_REQUIRED_%s";
    public static final String MESSAGE = "Product stock exceeded";

    private int index = 0;

    public ProductStockExceededError() {
        super(MESSAGE);
    }

    public void addProduct(UUID productId, Integer currentStock, Integer requiredStock) {
        addError(String.format(ERROR_CODE_PRODUCT, index), productId.toString());
        addError(String.format(ERROR_CODE_CURRENT, index), currentStock.toString());
        addError(String.format(ERROR_CODE_REQUIRED, index), requiredStock.toString());
        index += 1;
    }

}
