package com.fakecompany.order_management.controllers;

import com.fakecompany.order_management.api.OrderBuyerDetailsPatchV1Api;
import com.fakecompany.order_management.api.dto.OrderBuyerDetailsDto;
import com.fakecompany.order_management.api.dto.OrderDto;
import com.fakecompany.order_management.api.dto.OrderSeatDto;
import com.fakecompany.order_management.mappers.OrderToOrderDtoMapper;
import com.fakecompany.order_management.orders.application.update_buyer_details.OrderBuyerDetailsUpdater;
import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderBuyerDetails;
import com.fakecompany.order_management.orders.domain.OrderSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderBuyerDetailsPatchV1Controller implements OrderBuyerDetailsPatchV1Api {

    private final OrderBuyerDetailsUpdater orderBuyerDetailsUpdater;
    private final OrderToOrderDtoMapper orderToOrderDtoMapper;

    @Override
    public ResponseEntity<OrderDto> patchOrderBuyerDetails(UUID orderId, OrderBuyerDetailsDto orderBuyerDetailsDto) {
        Order update = orderBuyerDetailsUpdater.update(orderId, mapOrderBuyerDetailsDtoToOrderBuyerDetails(orderBuyerDetailsDto));
        return ResponseEntity.ok(orderToOrderDtoMapper.map(update));
    }

    private OrderBuyerDetails mapOrderBuyerDetailsDtoToOrderBuyerDetails(OrderBuyerDetailsDto orderBuyerDetailsDto) {
        if (orderBuyerDetailsDto == null) {
            return null;
        }
        return OrderBuyerDetails.builder()
                .email(orderBuyerDetailsDto.getEmail())
                .seat(mapOrderSeatDtoToOrderSeat(orderBuyerDetailsDto.getSeat()))
                .build();
    }

    private OrderSeat mapOrderSeatDtoToOrderSeat(OrderSeatDto seatDto) {
        if (seatDto == null) {
            return null;
        }
        return OrderSeat.builder()
                .seatLetter(seatDto.getSeatLetter() != null ?
                        seatDto.getSeatLetter().charAt(0) : null)
                .seatNumber(seatDto.getSeatNumber())
                .build();
    }
}
