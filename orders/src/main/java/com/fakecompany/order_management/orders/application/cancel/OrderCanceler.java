package com.fakecompany.order_management.orders.application.cancel;

import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderNotFoundError;
import com.fakecompany.order_management.orders.domain.OrderNotOpenError;
import com.fakecompany.order_management.orders.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCanceler {

    private final OrderRepository orderRepository;

    public Order cancel(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundError(orderId));

        if (Order.OrderStatus.OPEN != order.getStatus()) {
            throw new OrderNotOpenError(orderId);
        }

        order.updateStatus(Order.OrderStatus.DROPPED);
        return orderRepository.update(order);
    }

}
