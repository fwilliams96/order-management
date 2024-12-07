package com.fakecompany.order_management.orders.infrastructure.persistence.mongodb.repository;

import com.fakecompany.order_management.orders.infrastructure.persistence.mongodb.model.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SpringDataMongoDbOrderRepository extends MongoRepository<OrderEntity, UUID> {
}
