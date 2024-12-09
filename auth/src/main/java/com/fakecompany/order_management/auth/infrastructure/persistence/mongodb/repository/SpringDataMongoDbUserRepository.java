package com.fakecompany.order_management.auth.infrastructure.persistence.mongodb.repository;

import com.fakecompany.order_management.auth.infrastructure.persistence.mongodb.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataMongoDbUserRepository extends MongoRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

}
