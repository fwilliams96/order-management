package com.fakecompany.order_management.orders.application.finish;

import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderNotFoundError;
import com.fakecompany.order_management.orders.domain.OrderPayment;
import com.fakecompany.order_management.orders.domain.OrderRepository;
import com.fakecompany.order_management.payments.application.create.PaymentCreator;
import com.fakecompany.order_management.payments.domain.Payment;
import com.fakecompany.order_management.products.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderFinisher {

    private final OrderRepository orderRepository;
    private final PaymentCreator paymentCreator;

    private static final ZoneId MADRID_TIMEZONE = ZoneId.of("Europe/Madrid");

    public Order finish(UUID orderId, OrderPayment orderPayment) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundError(orderId));
        double totalPrice = order.getProducts().stream()
                .map(Product::getPrice)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .totalPrice(BigDecimal.valueOf(totalPrice))
                .cardToken(orderPayment.getCardToken())
                .paymentDate(LocalDateTime.now(MADRID_TIMEZONE))
                .status(orderPayment.getStatus())
                .gateway(orderPayment.getGateway())
                .build();
        Payment paymentInserted = paymentCreator.create(payment);
        order.updatePayment(paymentInserted);
        order.updateStatus(Order.OrderStatus.FINISHED);
        return orderRepository.update(order);
    }

}
