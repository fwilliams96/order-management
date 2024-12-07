package com.fakecompany.order_management.payments.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@Builder
public class Payment {

    @Getter
    @ToString
    @RequiredArgsConstructor
    public enum PaymentStatus {

        PAID(1),
        PAYMENT_FAILED(2),
        OFFLINE_PAYMENT(3);

        private final Integer value;

        public static PaymentStatus from(Integer value) {
            for (PaymentStatus paymentStatus: PaymentStatus.values()) {
                if (paymentStatus.value.equals(value)) {
                    return paymentStatus;
                }
            }
            return null;
        }
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    public enum PaymentGateway {

        STRIPE("STRIPE");

        private final String value;

        public static PaymentGateway from(String value) {
            for (PaymentGateway paymentGateway: PaymentGateway.values()) {
                if (paymentGateway.value.equalsIgnoreCase(value)) {
                    return paymentGateway;
                }
            }
            return null;
        }
    }

    private UUID id;
    private BigDecimal totalPrice;
    private String cardToken;
    private PaymentStatus status;
    private LocalDateTime paymentDate;
    private PaymentGateway gateway;

}

