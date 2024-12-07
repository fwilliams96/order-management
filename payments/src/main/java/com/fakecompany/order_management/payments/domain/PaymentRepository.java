package com.fakecompany.order_management.payments.domain;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment create(Payment payment);

    Optional<Payment> findById(UUID id);

}
