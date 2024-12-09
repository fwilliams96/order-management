package com.fakecompany.order_management.orders.application.add_product;

import com.fakecompany.order_management.categories.domain.Category;
import com.fakecompany.order_management.orders.domain.*;
import com.fakecompany.order_management.products.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

class OrderProductAdderTest {

    @InjectMocks
    OrderProductAdder orderProductAdder;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_not_add_due_to_order_not_found() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        doReturn(Optional.empty())
                .when(orderRepository)
                .findById(eq(orderId));

        assertThrows(OrderNotFoundError.class, () -> orderProductAdder.add(orderId, productId));
    }

    @Test
    void should_not_add_due_to_product_not_found() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        doReturn(Optional.of(orderMock(orderId)))
                .when(orderRepository)
                .findById(eq(orderId));

        doReturn(Optional.empty())
                .when(productRepository)
                .findById(eq(productId));

        assertThrows(ProductNotFoundError.class, () -> orderProductAdder.add(orderId, productId));
    }

    @Test
    void should_not_add_due_to_exceeded_stock() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        doReturn(Optional.of(orderWithProductsMock(orderId, productId)))
                .when(orderRepository)
                .findById(eq(orderId));

        doReturn(Optional.of(productMock(productId, 0)))
                .when(productRepository)
                .findById(eq(productId));

        assertThrows(ProductStockExceededError.class, () -> orderProductAdder.add(orderId, productId));
    }

    @Test
    void should_not_add_due_to_insufficient_stock() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        doReturn(Optional.of(orderMock(orderId)))
                .when(orderRepository)
                .findById(eq(orderId));

        doReturn(Optional.of(productMock(productId, 0)))
                .when(productRepository)
                .findById(eq(productId));

        assertThrows(ProductStockNotEnoughError.class, () -> orderProductAdder.add(orderId, productId));
    }

    @Test
    void should_add() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Order order = orderMock(orderId);
        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(eq(orderId));

        doReturn(Optional.of(productMock(productId, 1)))
                .when(productRepository)
                .findById(eq(productId));

        doAnswer(returnsFirstArg())
                .when(orderRepository)
                .update(any());

        Order add = orderProductAdder.add(orderId, productId);
        assertEquals(1, add.getProducts().size());
    }

    private Order orderMock(UUID orderId) {
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
                .status(Order.OrderStatus.OPEN)
                .build();
    }

    private Order orderWithProductsMock(UUID orderId, UUID productId) {
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
                                productMock(productId, 0)
                        )
                )
                .paymentDetails(null)
                .status(Order.OrderStatus.OPEN)
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