package com.fakecompany.order_management.payments.infrastructure.persistence.mongdb.model;

import com.fakecompany.order_management.shared.infrastructure.persistence.UuidIdentifiedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "payments")
public class PaymentEntity extends UuidIdentifiedEntity {

    private BigDecimal totalPrice;
    private String cardToken;
    private int status = 0;
    private String gateway;
    private LocalDateTime paymentDate;

}
