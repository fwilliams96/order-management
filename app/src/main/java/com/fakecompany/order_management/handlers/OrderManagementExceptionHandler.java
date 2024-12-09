package com.fakecompany.order_management.handlers;

import com.fakecompany.order_management.auth.domain.UserAlreadyExistsError;
import com.fakecompany.order_management.auth.domain.UserInvalidCredentialsError;
import com.fakecompany.order_management.auth.domain.UserNotFoundError;
import com.fakecompany.order_management.api.dto.ErrorDto;
import com.fakecompany.order_management.api.dto.ErrorMessageDto;
import com.fakecompany.order_management.orders.domain.OrderNotFoundError;
import com.fakecompany.order_management.orders.domain.OrderNotOpenError;
import com.fakecompany.order_management.products.domain.ProductNotFoundError;
import com.fakecompany.order_management.products.domain.ProductStockExceededError;
import com.fakecompany.order_management.shared.domain.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class OrderManagementExceptionHandler {

    @ExceptionHandler(ProductStockExceededError.class)
    @ResponseStatus(value = BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorDto> productStockExceeded(
            HttpServletRequest request,
            ProductStockExceededError exception
    ) {
        log.error("Product stock exceeded", exception);
        return buildErrorResponse(exception, BAD_REQUEST, request);
    }

    @ExceptionHandler(OrderNotOpenError.class)
    @ResponseStatus(value = BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorDto> orderNotOpen(
            HttpServletRequest request,
            OrderNotOpenError exception
    ) {
        log.error("Order not open", exception);
        return buildErrorResponse(exception, BAD_REQUEST, request);
    }

    @ExceptionHandler(UserAlreadyExistsError.class)
    @ResponseStatus(value = BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorDto> userAlreadyExists(
            HttpServletRequest request,
            UserAlreadyExistsError exception
    ) {
        log.error("User already exists", exception);
        return buildErrorResponse(exception, BAD_REQUEST, request);
    }

    @ExceptionHandler(UserNotFoundError.class)
    @ResponseStatus(value = NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ErrorDto> userNotFound(
            HttpServletRequest request,
            UserNotFoundError exception
    ) {
        log.error("User not found exists", exception);
        return buildErrorResponse(exception, NOT_FOUND, request);
    }

    @ExceptionHandler(UserInvalidCredentialsError.class)
    @ResponseStatus(value = BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorDto> userCredentialsInvalid(
            HttpServletRequest request,
            UserInvalidCredentialsError exception
    ) {
        log.error("User invalid credentials", exception);
        return buildErrorResponse(exception, BAD_REQUEST, request);
    }

    @ExceptionHandler(ProductNotFoundError.class)
    @ResponseStatus(value = NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ErrorDto> productNotFound(
            HttpServletRequest request,
            ProductNotFoundError exception
    ) {
        log.error("Product not found", exception);
        return buildErrorResponse(exception, NOT_FOUND, request);
    }

    @ExceptionHandler(OrderNotFoundError.class)
    @ResponseStatus(value = NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ErrorDto> orderNotFound(
            HttpServletRequest request,
            OrderNotFoundError exception
    ) {
        log.error("Order not found", exception);
        return buildErrorResponse(exception, NOT_FOUND, request);
    }

    private ResponseEntity<ErrorDto> buildErrorResponse(
            BaseException ex,
            HttpStatus status,
            HttpServletRequest request
    ) {
        ErrorDto errorResponse = new ErrorDto();
        errorResponse.setTimestamp(new Date().getTime());
        errorResponse.setStatus(status.value());
        errorResponse.setError(ex.getMessage());
        errorResponse.setPath(request.getServletPath());

        if (ex.hasErrors()) {
            ex.getErrors().forEach(error -> errorResponse.addErrorsItem(
                    new ErrorMessageDto().code(error.getCode()).message(error.getMessage())
            ));
        }

        return ResponseEntity.status(status).body(errorResponse);
    }

}