package com.fakecompany.order_management.orders.application.add_product;

import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderNotFoundError;
import com.fakecompany.order_management.orders.domain.OrderRepository;
import com.fakecompany.order_management.products.domain.*;
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

        int numOfProductsOfTheSameType = order.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .mapToInt(p -> 1)
                .sum();

        // If the current amount of products is higher than the current stock, throw error
        if (numOfProductsOfTheSameType > product.getStock()) {
            ProductStockExceededError productStockExceededError = new ProductStockExceededError();
            productStockExceededError.addProduct(productId, product.getStock(), numOfProductsOfTheSameType);
            throw productStockExceededError;
        }

        // If the current amount of products plus the new unit is higher, then throw error
        if ((numOfProductsOfTheSameType + 1) > product.getStock()) {
            ProductStockNotEnoughError productStockNotEnoughError = new ProductStockNotEnoughError();
            productStockNotEnoughError.addProduct(productId, product.getStock(), numOfProductsOfTheSameType);
            throw productStockNotEnoughError;
        }

        order.addProduct(product);
        return orderRepository.update(order);
    }

}
