package com.fakecompany.order_management.controllers;

import com.fakecompany.order_management.api.OrderProductDeleteV1Api;
import com.fakecompany.order_management.api.dto.OrderDto;
import com.fakecompany.order_management.mappers.OrderToOrderDtoMapper;
import com.fakecompany.order_management.orders.application.delete_product.OrderProductDeleter;
import com.fakecompany.order_management.orders.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderProductDeleteV1Controller implements OrderProductDeleteV1Api {

    private final OrderProductDeleter orderProductDeleter;
    private final OrderToOrderDtoMapper orderToOrderDtoMapper;

    @Override
    public ResponseEntity<OrderDto> deleteOrderProduct(UUID orderId, UUID productId) {
        Order delete = orderProductDeleter.delete(orderId, productId);
        return ResponseEntity.ok(orderToOrderDtoMapper.map(delete));
    }
}
