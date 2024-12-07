package com.fakecompany.order_management.orders.infrastructure.persistence.mongodb.model;

import com.fakecompany.order_management.shared.infrastructure.persistence.UuidIdentifiedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "orders")
public class OrderEntity extends UuidIdentifiedEntity {

    private List<UUID> productsIds;
    private UUID paymentId;
    private int status = 0;
    private OrderBuyerDetailsEntity buyerDetails;

    @Data
    public static class OrderBuyerDetailsEntity {
        private String email;
        private OrderSeatEntity seat;
    }

    @Data
    public static class OrderSeatEntity {

        private Character seatLetter;
        private Integer seatNumber;

    }
}
