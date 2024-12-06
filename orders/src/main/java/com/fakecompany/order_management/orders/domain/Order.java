package com.fakecompany.order_management.orders.domain;

import com.fakecompany.order_management.payments.domain.PaymentDetails;
import com.fakecompany.order_management.products.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
public class Order {

    @Getter
    @ToString
    @RequiredArgsConstructor
    public enum OrderStatus {

        OPEN(0),
        DROPPED(1),
        FINISHED(2);

        private final Integer value;
    }

    private List<Product> products;
    private PaymentDetails paymentDetails;
    private OrderStatus status;
    private BuyerDetails buyerDetails;

}
