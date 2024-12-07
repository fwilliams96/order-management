package com.fakecompany.order_management.orders.domain;

import com.fakecompany.order_management.payments.domain.Payment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderPayment {

    private String cardToken;
    private Payment.PaymentStatus status;
    private Payment.PaymentGateway gateway;

}
