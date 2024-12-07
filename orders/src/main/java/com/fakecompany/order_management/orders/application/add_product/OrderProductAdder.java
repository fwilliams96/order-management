package com.fakecompany.order_management.orders.application.add_product;

import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderNotFoundError;
import com.fakecompany.order_management.orders.domain.OrderRepository;
import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductNotFoundError;
import com.fakecompany.order_management.products.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderProductAdder {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public Order add(UUID orderId, UUID productId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundError(orderId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundError(productId));
        order.addProduct(product);
        return orderRepository.update(order);
    }

}
