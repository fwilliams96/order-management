package com.fakecompany.order_management.categories.infrastructure.persistence.mongodb.repository;

import com.fakecompany.order_management.categories.infrastructure.persistence.mongodb.model.CategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SpringDataMongoDbCategoryRepository extends MongoRepository<CategoryEntity, UUID> {
}
