package com.fakecompany.order_management.payments.infrastructure.persistence.mongdb;

import com.fakecompany.order_management.payments.domain.Payment;
import com.fakecompany.order_management.payments.domain.PaymentRepository;
import com.fakecompany.order_management.payments.infrastructure.persistence.mongdb.model.PaymentEntity;
import com.fakecompany.order_management.payments.infrastructure.persistence.mongdb.repository.SpringDataMongoDbPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MongoDbPaymentRepository implements PaymentRepository {

    private final SpringDataMongoDbPaymentRepository springDataMongoDbPaymentRepository;

    @Override
    public Payment create(Payment payment) {
        PaymentEntity insert = springDataMongoDbPaymentRepository.insert(mapPaymentToPaymentEntity(payment));
        return mapPaymentEntityToPayment(insert);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        Optional<PaymentEntity> byId = springDataMongoDbPaymentRepository.findById(id);
        return byId.map(this::mapPaymentEntityToPayment);
    }

    private PaymentEntity mapPaymentToPaymentEntity(Payment payment) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setId(payment.getId());
        paymentEntity.setPaymentDate(payment.getPaymentDate());
        paymentEntity.setStatus(payment.getStatus().getValue());
        paymentEntity.setGateway(payment.getGateway().getValue());
        paymentEntity.setTotalPrice(payment.getTotalPrice());
        paymentEntity.setCardToken(payment.getCardToken());
        return paymentEntity;
    }

    private Payment mapPaymentEntityToPayment(PaymentEntity paymentEntity) {
        if (paymentEntity == null) {
            return null;
        }
        return Payment.builder()
                .id(paymentEntity.getId())
                .totalPrice(paymentEntity.getTotalPrice())
                .cardToken(paymentEntity.getCardToken())
                .status(Payment.PaymentStatus.from(paymentEntity.getStatus()))
                .paymentDate(paymentEntity.getPaymentDate())
                .gateway(Payment.PaymentGateway.from(paymentEntity.getGateway()))
                .build();
    }
}
