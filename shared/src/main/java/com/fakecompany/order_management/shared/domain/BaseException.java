package com.fakecompany.order_management.shared.domain;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BaseException extends RuntimeException {

    private final String message;

    private final List<ExceptionError> errors;

    public BaseException(String message) {
        super(message);
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public BaseException(String message, String code, Exception innerException) {
        super(message, innerException);
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public void addError(String code, String message) {
        errors.add(new ExceptionError(code, message));
    }

    public boolean hasErrors() {
        return !Objects.isNull(errors) && !errors.isEmpty();
    }

    public List<ExceptionError> getErrors() {
        return hasErrors() ? List.copyOf(errors) : Collections.emptyList();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Getter
    public static class ExceptionError implements Serializable {
        private final String code;
        private final String message;

        public ExceptionError(String code, String message) {
            this.code = code;
            this.message = message;
        }

    }

}
