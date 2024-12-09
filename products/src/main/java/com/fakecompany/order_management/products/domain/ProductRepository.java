package com.fakecompany.order_management.products.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findById(UUID id);

    Product update(Product product);

}
