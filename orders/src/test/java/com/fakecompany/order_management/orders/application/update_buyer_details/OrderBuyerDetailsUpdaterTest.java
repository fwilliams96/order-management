package com.fakecompany.order_management.orders.application.update_buyer_details;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

class OrderBuyerDetailsUpdaterTest {

    @InjectMocks
    OrderBuyerDetailsUpdater orderBuyerDetailsUpdater;

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

        assertThrows(OrderNotFoundError.class, () -> orderBuyerDetailsUpdater.update(orderId, orderBuyerDetailsMock()));
    }

    @Test
    void should_not_update_due_to_order_not_open() {
        UUID orderId = UUID.randomUUID();
        Order order = orderMock(orderId, Order.OrderStatus.FINISHED);
        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(eq(orderId));

        assertThrows(OrderNotOpenError.class, () -> orderBuyerDetailsUpdater.update(orderId, orderBuyerDetailsMock()));
    }

    @Test
    void should_update() {
        UUID orderId = UUID.randomUUID();
        Order order = orderMock(orderId, Order.OrderStatus.OPEN);
        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(eq(orderId));

        doAnswer(returnsFirstArg())
                .when(orderRepository)
                .update(any());

        Order update = orderBuyerDetailsUpdater.update(orderId, orderBuyerDetailsMock());
        assertNotNull(update);
        assertNotNull(update.getBuyerDetails());
        assertNotNull(update.getBuyerDetails().getEmail());
        assertNotNull(update.getBuyerDetails().getSeat());
        assertNotNull(update.getBuyerDetails().getSeat().getSeatNumber());
        assertNotNull(update.getBuyerDetails().getSeat().getSeatLetter());
    }

    private OrderBuyerDetails orderBuyerDetailsMock() {
        return OrderBuyerDetails.builder()
                .email("fwmcomputer@gmail.com")
                .seat(
                        OrderSeat.builder()
                                .seatNumber(1)
                                .seatLetter('A')
                                .build()
                )
                .build();
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