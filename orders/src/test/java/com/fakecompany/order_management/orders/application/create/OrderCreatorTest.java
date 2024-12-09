package com.fakecompany.order_management.orders.application.create;

import com.fakecompany.order_management.categories.domain.Category;
import com.fakecompany.order_management.orders.domain.NewOrder;
import com.fakecompany.order_management.orders.domain.Order;
import com.fakecompany.order_management.orders.domain.OrderRepository;
import com.fakecompany.order_management.orders.domain.OrderSeat;
import com.fakecompany.order_management.products.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

class OrderCreatorTest {

    @InjectMocks
    OrderCreator orderCreator;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_not_create_due_to_product_not_found() {
        UUID productId = UUID.randomUUID();

        doReturn(Optional.empty())
                .when(productRepository)
                .findById(eq(productId));

        assertThrows(ProductNotFoundError.class, () -> orderCreator.create(newOrderMock(productId)));
    }

    @Test
    void should_not_create_due_to_insufficient_stock() {
        UUID productId = UUID.randomUUID();

        doReturn(Optional.of(productMock(productId, 0)))
                .when(productRepository)
                .findById(eq(productId));

        assertThrows(ProductStockNotEnoughError.class, () -> orderCreator.create(newOrderMock(productId)));
    }

    @Test
    void should_create() {
        UUID productId = UUID.randomUUID();
        Product product = productMock(productId, 1);

        doReturn(Optional.of(product))
                .when(productRepository)
                .findById(eq(productId));

        doAnswer(returnsFirstArg())
                .when(orderRepository)
                .create(any());

        Order order = orderCreator.create(newOrderMock(productId));
        assertNotNull(order);
        assertNotNull(order.getId());
        assertEquals(product.getPrice(), order.getTotalPrice());
        assertEquals(1, order.getProducts().size());
        assertEquals(Order.OrderStatus.OPEN, order.getStatus());
    }


    private NewOrder newOrderMock(UUID productId) {
        return NewOrder.builder()
                .seat(
                        OrderSeat.builder()
                                .seatLetter('A')
                                .seatNumber(1)
                                .build()
                )
                .productIds(List.of(productId))
                .userId(UUID.randomUUID())
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