package com.fakecompany.order_management.auth.infrastructure.persistence.mongodb.model;

import com.fakecompany.order_management.shared.infrastructure.persistence.UuidIdentifiedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "users")
public class UserEntity extends UuidIdentifiedEntity {

    private String name;
    private String email;
    private String password;
    private Set<String> roles;

}
