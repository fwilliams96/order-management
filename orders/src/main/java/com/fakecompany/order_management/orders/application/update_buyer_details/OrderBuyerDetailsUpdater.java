package com.fakecompany.order_management.orders.application.update_buyer_details;

import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderBuyerDetails;
import com.fakecompany.order_management.orders.domain.OrderNotFoundError;
import com.fakecompany.order_management.orders.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderBuyerDetailsUpdater {

    private final OrderRepository orderRepository;

    public Order update(UUID orderId, OrderBuyerDetails orderBuyerDetails) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundError(orderId));
        if (orderBuyerDetails.getSeat() != null) {
            order.updateSeat(orderBuyerDetails.getSeat());
        }
        if (orderBuyerDetails.getEmail() != null) {
            order.updateEmail(orderBuyerDetails.getEmail());
        }
        return orderRepository.update(order);
    }

}
