package com.fakecompany.order_management.orders.domain;

import com.fakecompany.order_management.payments.domain.Payment;
import com.fakecompany.order_management.products.domain.Product;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        public static OrderStatus from(Integer value) {
            for (OrderStatus orderStatus: OrderStatus.values()) {
                if (orderStatus.value.equals(value)) {
                    return orderStatus;
                }
            }
            return null;
        }
    }

    private UUID id;
    private List<Product> products;
    private Payment paymentDetails;
    private OrderStatus status;
    private OrderBuyerDetails buyerDetails;
    private UUID userId;
    private BigDecimal totalPrice;

    public void updateSeat(OrderSeat orderSeat) {
        if (buyerDetails == null) {
            buyerDetails = OrderBuyerDetails.builder()
                    .seat(orderSeat)
                    .build();
            return;
        }
        buyerDetails.updateSeat(orderSeat);
    }

    public void updateEmail(String email) {
        if (buyerDetails == null) {
            buyerDetails = OrderBuyerDetails.builder()
                    .email(email)
                    .build();
            return;
        }
        buyerDetails.updateEmail(email);
    }

    public void updatePayment(Payment paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public void addProduct(Product product) {
        if (products == null) {
            products = new ArrayList<>();
        }
        products.add(product);
        if (totalPrice == null) {
            totalPrice = BigDecimal.ZERO;
        }
        totalPrice = totalPrice.add(product.getPrice());
    }

    public void deleteProduct(Product product) {
        if (products == null) {
            return;
        }
        products.remove(product);
        if (totalPrice != null) {
            totalPrice = totalPrice.subtract(product.getPrice());
        }
    }

}
