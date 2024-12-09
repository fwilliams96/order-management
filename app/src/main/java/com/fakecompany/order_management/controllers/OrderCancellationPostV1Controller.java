package com.fakecompany.order_management.controllers;

import com.fakecompany.order_management.api.OrderCancellationPostV1Api;
import com.fakecompany.order_management.api.dto.OrderDto;
import com.fakecompany.order_management.mappers.OrderToOrderDtoMapper;
import com.fakecompany.order_management.orders.application.cancel.OrderCanceler;
import com.fakecompany.order_management.orders.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderCancellationPostV1Controller implements OrderCancellationPostV1Api {

    private final OrderCanceler orderCanceler;
    private final OrderToOrderDtoMapper orderToOrderDtoMapper;

    @Override
    public ResponseEntity<OrderDto> postOrderCancellation(UUID orderId) {
        Order cancel = orderCanceler.cancel(orderId);
        return ResponseEntity.ok(orderToOrderDtoMapper.map(cancel));
    }
}
