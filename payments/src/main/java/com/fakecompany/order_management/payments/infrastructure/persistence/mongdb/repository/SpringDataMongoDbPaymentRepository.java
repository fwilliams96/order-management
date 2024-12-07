package com.fakecompany.order_management.payments.infrastructure.persistence.mongdb.repository;

import com.fakecompany.order_management.payments.infrastructure.persistence.mongdb.model.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface SpringDataMongoDbPaymentRepository extends MongoRepository<PaymentEntity, UUID> {
}
