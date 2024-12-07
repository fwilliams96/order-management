package com.fakecompany.order_management.orders.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Builder
public class NewOrder {

    private List<UUID> productsIds;
    private OrderSeat seat;

}
