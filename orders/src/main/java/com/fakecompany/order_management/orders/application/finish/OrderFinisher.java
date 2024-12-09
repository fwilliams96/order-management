package com.fakecompany.order_management.orders.application.finish;

import com.fakecompany.order_management.orders.domain.*;
import com.fakecompany.order_management.payments.application.create.PaymentCreator;
import com.fakecompany.order_management.payments.domain.Payment;
import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductRepository;
import com.fakecompany.order_management.products.domain.ProductStockExceededError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderFinisher {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentCreator paymentCreator;

    private static final ZoneId MADRID_TIMEZONE = ZoneId.of("Europe/Madrid");

    // Synchronized to avoid finishing multiple orders with the same products (stock can't be exceeded)
    public synchronized Order finish(UUID orderId, OrderPayment orderPayment) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundError(orderId));

        if (Order.OrderStatus.OPEN != order.getStatus()) {
            throw new OrderNotOpenError(orderId);
        }

        // Check stock
        Map<Product, Integer> productDemandMap = order.getProducts().stream()
                .collect(Collectors.groupingBy(
                        product -> product,
                        Collectors.summingInt(product -> 1)
                ));

        Map<Product, Integer> insufficientStockMap = productDemandMap.entrySet().stream()
                .filter(entry -> {
                    Product product = entry.getKey();
                    int totalDemanded = entry.getValue();
                    int availableStock = product.getStock();
                    return totalDemanded > availableStock;
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        if (!insufficientStockMap.isEmpty()) {
            log.debug("Products with stock exceeded: {}", insufficientStockMap);
            ProductStockExceededError exceededError = new ProductStockExceededError();
            insufficientStockMap.forEach((key, value) -> exceededError.addProduct(
                    key.getId(),
                    key.getStock(),
                    value
            ));
            throw exceededError;
        }
        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .totalPrice(order.getTotalPrice())
                .cardToken(orderPayment.getCardToken())
                .paymentDate(LocalDateTime.now(MADRID_TIMEZONE))
                .status(orderPayment.getStatus())
                .gateway(orderPayment.getGateway())
                .build();

        // Update product stock
        productDemandMap.forEach((product, stockRequired) -> {
            product.reduceStock(stockRequired);
            productRepository.update(product);
        });

        Payment paymentInserted = paymentCreator.create(payment);
        order.updatePayment(paymentInserted);
        order.updateStatus(Order.OrderStatus.FINISHED);
        return orderRepository.update(order);
    }

}
