package com.fakecompany.order_management.orders.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class BuyerDetails {

    private String email;
    private Character seatLetter;
    private Integer seatNumber;

}
