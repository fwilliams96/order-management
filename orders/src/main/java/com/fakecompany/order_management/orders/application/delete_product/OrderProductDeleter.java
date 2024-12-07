package com.fakecompany.order_management.orders.application.delete_product;

import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderNotFoundError;
import com.fakecompany.order_management.orders.domain.OrderRepository;
import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductNotFoundError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderProductDeleter {

    private final OrderRepository orderRepository;

    public Order delete(UUID orderId, UUID productId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundError(orderId));
        Product product = order.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findAny()
                .orElseThrow(() -> new ProductNotFoundError(productId));
        order.getProducts().remove(product);
        return orderRepository.update(order);
    }

}
