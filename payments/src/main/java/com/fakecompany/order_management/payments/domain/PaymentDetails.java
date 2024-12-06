package com.fakecompany.order_management.payments.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
public class PaymentDetails {

    @Getter
    @ToString
    @RequiredArgsConstructor
    public enum PaymentStatus {

        PENDING(0),
        PAID(1),
        FAILED(2),
        CANCELLED(3);

        private final Integer value;
    }

    private BigDecimal totalPrice;
    private String cardToken;
    private PaymentStatus status;
    private LocalDateTime occurredDate;
    private String gateway;

}

