package com.fakecompany.order_management.products.infrastructure.persistence.mongodb.model;

import com.fakecompany.order_management.shared.infrastructure.persistence.UuidIdentifiedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "products")
public class ProductEntity extends UuidIdentifiedEntity {

    private String name;
    private BigDecimal price;
    private ProductImageEntity image;
    private UUID categoryId;
    private Integer stock;

    @Data
    public static class ProductImageEntity {

        private String url;
    }

}