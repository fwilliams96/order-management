package com.fakecompany.order_management.controllers;

import com.fakecompany.order_management.api.OrdersPostV1Api;
import com.fakecompany.order_management.api.dto.NewOrderDto;
import com.fakecompany.order_management.api.dto.OrderDto;
import com.fakecompany.order_management.api.dto.OrderSeatDto;
import com.fakecompany.order_management.mappers.OrderToOrderDtoMapper;
import com.fakecompany.order_management.orders.application.create.OrderCreator;
import com.fakecompany.order_management.orders.domain.NewOrder;
import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class OrdersPostV1Controller implements OrdersPostV1Api {

    private final OrderCreator orderCreator;
    private final OrderToOrderDtoMapper orderToOrderDtoMapper;

    @Override
    public ResponseEntity<OrderDto> postOrders(NewOrderDto newOrderDto) {
        Order order = orderCreator.create(
                NewOrder.builder()
                        .productsIds(newOrderDto.getProductsIds())
                        .seat(mapOrderSeatDtoToOrderSeat(newOrderDto.getSeat()))
                        .build()
        );
        return ResponseEntity.ok(orderToOrderDtoMapper.map(order));
    }

    private OrderSeat mapOrderSeatDtoToOrderSeat(OrderSeatDto seatDto) {
        return OrderSeat.builder()
                .seatNumber(seatDto.getSeatNumber())
                .seatLetter(seatDto.getSeatLetter().charAt(0))
                .build();
    }
}