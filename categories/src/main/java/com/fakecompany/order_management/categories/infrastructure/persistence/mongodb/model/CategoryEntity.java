package com.fakecompany.order_management.categories.infrastructure.persistence.mongodb.model;

import com.fakecompany.order_management.shared.infrastructure.persistence.UuidIdentifiedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "categories")
public class CategoryEntity extends UuidIdentifiedEntity {

    private String name;
    private UUID parentId;

}
