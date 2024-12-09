package com.fakecompany.order_management.orders.application.cancel;

import com.fakecompany.order_management.orders.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

class OrderCancelerTest {

    @InjectMocks
    OrderCanceler orderCanceler;

    @Mock
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_not_cancel_due_to_order_not_found() {
        UUID orderId = UUID.randomUUID();
        doReturn(Optional.empty())
                .when(orderRepository)
                .findById(eq(orderId));

        assertThrows(OrderNotFoundError.class, () -> orderCanceler.cancel(orderId));
    }

    @Test
    void should_not_cancel_due_to_order_not_open() {
        UUID orderId = UUID.randomUUID();
        Order order = orderMock(orderId, Order.OrderStatus.FINISHED);
        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(eq(orderId));

        assertThrows(OrderNotOpenError.class, () -> orderCanceler.cancel(orderId));
    }

    @Test
    void should_cancel() {
        UUID orderId = UUID.randomUUID();
        Order order = orderMock(orderId, Order.OrderStatus.OPEN);
        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(eq(orderId));

        doAnswer(returnsFirstArg())
                .when(orderRepository)
                .update(any());

        Order cancel = orderCanceler.cancel(orderId);
        assertEquals(Order.OrderStatus.DROPPED, cancel.getStatus());
    }

    private Order orderMock(UUID orderId, Order.OrderStatus orderStatus) {
        return Order.builder()
                .id(orderId)
                .totalPrice(BigDecimal.ZERO)
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
                .products(new ArrayList<>())
                .paymentDetails(null)
                .status(orderStatus)
                .build();
    }


}