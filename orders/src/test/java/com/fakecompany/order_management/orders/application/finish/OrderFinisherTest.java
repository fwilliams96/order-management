package com.fakecompany.order_management.orders.application.finish;

import com.fakecompany.order_management.categories.domain.Category;
import com.fakecompany.order_management.orders.domain.*;
import com.fakecompany.order_management.payments.application.create.PaymentCreator;
import com.fakecompany.order_management.payments.domain.Payment;
import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductImage;
import com.fakecompany.order_management.products.domain.ProductRepository;
import com.fakecompany.order_management.products.domain.ProductStockExceededError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderFinisherTest {

    @InjectMocks
    OrderFinisher orderFinisher;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    PaymentCreator paymentCreator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_not_finish_due_to_order_not_found() {
        UUID orderId = UUID.randomUUID();
        OrderPayment orderPayment = orderPaymentMock();
        doReturn(Optional.empty())
                .when(orderRepository)
                .findById(eq(orderId));

        assertThrows(OrderNotFoundError.class, () -> orderFinisher.finish(orderId, orderPayment));
    }

    @Test
    void should_not_finish_due_to_order_not_open() {
        UUID orderId = UUID.randomUUID();
        OrderPayment orderPayment = orderPaymentMock();
        Order order = orderWithProductsMock(orderId, Order.OrderStatus.DROPPED, 1);
        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(eq(orderId));

        assertThrows(OrderNotOpenError.class, () -> orderFinisher.finish(orderId, orderPayment));
    }

    @Test
    void should_not_finish_due_to_exceeded_stock() {
        UUID orderId = UUID.randomUUID();
        OrderPayment orderPayment = orderPaymentMock();
        Order order = orderWithProductsMock(orderId, Order.OrderStatus.OPEN, 0);
        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(eq(orderId));

        assertThrows(ProductStockExceededError.class, () -> orderFinisher.finish(orderId, orderPayment));
    }

    @Test
    void should_finish() {
        UUID orderId = UUID.randomUUID();
        OrderPayment orderPayment = orderPaymentMock();
        Order order = orderWithProductsMock(orderId, Order.OrderStatus.OPEN, 1);

        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(eq(orderId));

        doAnswer(returnsFirstArg())
                .when(paymentCreator)
                .create(any());

        doAnswer(returnsFirstArg())
                .when(orderRepository)
                .update(any());

        Order finish = orderFinisher.finish(orderId, orderPayment);
        assertNotNull(finish);
        assertEquals(Order.OrderStatus.FINISHED, finish.getStatus());
        assertEquals(1, order.getProducts().size());
        assertEquals(0, order.getProducts().get(0).getStock());
        verify(productRepository, atLeastOnce()).update(any());
    }

    private OrderPayment orderPaymentMock() {
        return OrderPayment.builder()
                .cardToken("tok_1Lxyz12345")
                .status(Payment.PaymentStatus.PAID)
                .gateway(Payment.PaymentGateway.STRIPE)
                .build();
    }

    private Order orderWithProductsMock(UUID orderId, Order.OrderStatus orderStatus, Integer productStock) {
        return Order.builder()
                .id(orderId)
                .totalPrice(BigDecimal.valueOf(50d))
                .userId(UUID.randomUUID())
                .buyerDetails(
                        OrderBuyerDetails.builder()
                                .seat(
                                        OrderSeat.builder()
                                                .seatLetter('A')
                                                .seatNumber(1)
                                                .build()
                                )
                                .build()
                )
                .products(
                        Collections.singletonList(
                                productMock(UUID.randomUUID(), productStock)
                        )
                )
                .paymentDetails(null)
                .status(orderStatus)
                .build();
    }

    private Product productMock(UUID productId, Integer stock) {
        return Product.builder()
                .id(productId)
                .stock(stock)
                .image(
                        ProductImage.builder()
                                .url("https://images.pexels.com/photos/121191/pexels-photo-121191.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
                                .build()
                )
                .name("Wine Deluxe")
                .price(BigDecimal.valueOf(50d))
                .category(
                        Category.builder()
                                .id(UUID.randomUUID())
                                .name("Wines")
                                .parent(null)
                                .build()
                )
                .build();
    }

}