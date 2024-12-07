package com.fakecompany.order_management.orders.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class OrderBuyerDetails {

    private String email;
    private OrderSeat seat;

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateSeat(OrderSeat seat) {
        this.seat = seat;
    }

}
