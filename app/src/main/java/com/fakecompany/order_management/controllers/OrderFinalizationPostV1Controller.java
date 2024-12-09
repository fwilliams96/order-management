package com.fakecompany.order_management.controllers;

import com.fakecompany.order_management.api.OrderFinalizationPostV1Api;
import com.fakecompany.order_management.api.dto.OrderDto;
import com.fakecompany.order_management.api.dto.OrderPaymentDto;
import com.fakecompany.order_management.mappers.OrderToOrderDtoMapper;
import com.fakecompany.order_management.orders.application.finish.OrderFinisher;
import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderPayment;
import com.fakecompany.order_management.payments.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderFinalizationPostV1Controller implements OrderFinalizationPostV1Api {

    private final OrderFinisher orderFinisher;
    private final OrderToOrderDtoMapper orderToOrderDtoMapper;

    @Override
    public ResponseEntity<OrderDto> postOrderFinalization(UUID orderId, OrderPaymentDto orderPaymentDto) {
        Order finish = orderFinisher.finish(orderId, mapOrderPaymentDtoToOrdePayment(orderPaymentDto));
        return ResponseEntity.ok(orderToOrderDtoMapper.map(finish));
    }

    private OrderPayment mapOrderPaymentDtoToOrdePayment(OrderPaymentDto orderPaymentDto) {
        if (orderPaymentDto == null) {
            return null;
        }
        return OrderPayment.builder()
                .cardToken(orderPaymentDto.getCardToken())
                .gateway(Payment.PaymentGateway.from(orderPaymentDto.getGateway().getValue()))
                .status(Payment.PaymentStatus.from(orderPaymentDto.getStatus().getValue()))
                .build();
    }
}
