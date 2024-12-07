package com.fakecompany.order_management.payments.application.create;

import com.fakecompany.order_management.payments.domain.Payment;
import com.fakecompany.order_management.payments.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCreator {

    private final PaymentRepository paymentRepository;

    public Payment create(Payment payment) {
        return paymentRepository.create(payment);
    }

}
