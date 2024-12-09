package com.fakecompany.order_management.orders.application.delete_product;

import com.fakecompany.order_management.categories.domain.Category;
import com.fakecompany.order_management.orders.domain.*;
import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductImage;
import com.fakecompany.order_management.products.domain.ProductNotFoundError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

class OrderProductDeleterTest {

    @InjectMocks
    OrderProductDeleter orderProductDeleter;

    @Mock
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_not_delete_due_to_order_not_found() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        doReturn(Optional.empty())
                .when(orderRepository)
                .findById(eq(orderId));

        assertThrows(OrderNotFoundError.class, () -> orderProductDeleter.delete(orderId, productId));
    }

    @Test
    void should_not_add_due_to_product_not_found() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        doReturn(Optional.of(orderMock(orderId)))
                .when(orderRepository)
                .findById(eq(orderId));

        assertThrows(ProductNotFoundError.class, () -> orderProductDeleter.delete(orderId, productId));
    }

    @Test
    void should_delete() {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Order order = orderWithProductsMock(orderId, productId);
        doReturn(Optional.of(order))
                .when(orderRepository)
                .findById(eq(orderId));

        doAnswer(returnsFirstArg())
                .when(orderRepository)
                .update(any());

        Order add = orderProductDeleter.delete(orderId, productId);
        assertEquals(0, add.getProducts().size());
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
                        new ArrayList<>(Arrays.asList(
                                productMock(productId, 0)
                        ))
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