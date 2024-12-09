package com.fakecompany.order_management.orders.application.update_buyer_details;

import com.fakecompany.order_management.orders.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderBuyerDetailsUpdater {

    private final OrderRepository orderRepository;

    public Order update(UUID orderId, OrderBuyerDetails orderBuyerDetails) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundError(orderId));

        if (Order.OrderStatus.OPEN != order.getStatus()) {
            throw new OrderNotOpenError(orderId);
        }

        if (orderBuyerDetails.getSeat() != null) {
            order.updateSeat(orderBuyerDetails.getSeat());
        }
        if (orderBuyerDetails.getEmail() != null) {
            order.updateEmail(orderBuyerDetails.getEmail());
        }
        return orderRepository.update(order);
    }

}
