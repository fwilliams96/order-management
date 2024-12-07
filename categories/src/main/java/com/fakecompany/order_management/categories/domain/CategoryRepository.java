package com.fakecompany.order_management.categories.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {

    List<Category> findAll();

    Optional<Category> findById(UUID categoryId);

}
