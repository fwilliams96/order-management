package com.fakecompany.order_management.products.infrastructure.persistence.mongodb.repository;

import com.fakecompany.order_management.products.infrastructure.persistence.mongodb.model.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SpringDataMongoDbProductRepository extends MongoRepository<ProductEntity, UUID> {
}
