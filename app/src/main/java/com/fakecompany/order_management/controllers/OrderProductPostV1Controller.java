package com.fakecompany.order_management.controllers;

import com.fakecompany.order_management.api.OrderProductPostV1Api;
import com.fakecompany.order_management.api.dto.OrderDto;
import com.fakecompany.order_management.api.dto.ProductIdDto;
import com.fakecompany.order_management.mappers.OrderToOrderDtoMapper;
import com.fakecompany.order_management.orders.application.add_product.OrderProductAdder;
import com.fakecompany.order_management.orders.domain.Order;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderProductPostV1Controller implements OrderProductPostV1Api {

    private final OrderProductAdder orderProductAdder;
    private final OrderToOrderDtoMapper orderToOrderDtoMapper;

    @Override
    public ResponseEntity<OrderDto> postOrderProduct(UUID orderId, ProductIdDto productIdDto) {
        Order add = orderProductAdder.add(orderId, productIdDto.getProductId());
        return ResponseEntity.ok(orderToOrderDtoMapper.map(add));
    }
}
