package com.fakecompany.order_management.categories.infrastructure.persistence.mongodb;

import com.fakecompany.order_management.categories.domain.Category;
import com.fakecompany.order_management.categories.domain.CategoryRepository;
import com.fakecompany.order_management.categories.infrastructure.persistence.mongodb.model.CategoryEntity;
import com.fakecompany.order_management.categories.infrastructure.persistence.mongodb.repository.SpringDataMongoDbCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MongoDbCategoryRepository implements CategoryRepository {

    private final SpringDataMongoDbCategoryRepository springDataMongoDbCategoryRepository;

    @Override
    public List<Category> findAll() {
        List<CategoryEntity> all = springDataMongoDbCategoryRepository.findAll();
        return all.stream().map(this::mapCategoryEntityToCategory).collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findById(UUID categoryId) {
        Optional<CategoryEntity> byId = springDataMongoDbCategoryRepository.findById(categoryId);
        return byId.map(this::mapCategoryEntityToCategory);
    }

    private Category mapCategoryEntityToCategory(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }

        Optional<Category> parentCategoryOpt = categoryEntity.getParentId() != null ?
                findById(categoryEntity.getParentId()) : Optional.empty();

        return Category.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .parent(parentCategoryOpt.orElse(null))
                .build();
    }
}
