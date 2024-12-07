package com.fakecompany.order_management.orders.domain;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order create(Order order);

    Order update(Order order);

    Optional<Order> findById(UUID id);

}
