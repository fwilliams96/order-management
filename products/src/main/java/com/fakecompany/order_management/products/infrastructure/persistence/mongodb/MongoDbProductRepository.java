package com.fakecompany.order_management.products.infrastructure.persistence.mongodb;

import com.fakecompany.order_management.categories.domain.CategoryRepository;
import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductImage;
import com.fakecompany.order_management.products.domain.ProductRepository;
import com.fakecompany.order_management.products.infrastructure.persistence.mongodb.model.ProductEntity;
import com.fakecompany.order_management.products.infrastructure.persistence.mongodb.repository.SpringDataMongoDbProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MongoDbProductRepository implements ProductRepository {

    private final SpringDataMongoDbProductRepository mongoDbProductRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Product> findAll() {
        List<ProductEntity> all = mongoDbProductRepository.findAll();
        return all.stream().map(this::mapProductEntityToProduct).toList();
    }

    private Product mapProductEntityToProduct(ProductEntity productEntity) {
        return Product.builder()
                .id(productEntity.getId())
                .price(productEntity.getPrice())
                .name(productEntity.getName())
                .image(mapProductImageEntityToProductImage(productEntity.getImage()))
                .category(categoryRepository.findById(productEntity.getCategoryId()).orElse(null))
                .build();
    }

    private ProductImage mapProductImageEntityToProductImage(ProductEntity.ProductImageEntity productImageEntity) {
        if (productImageEntity == null) {
            return null;
        }
        return ProductImage.builder()
                .url(productImageEntity.getUrl())
                .build();
    }

    @Override
    public Optional<Product> findById(UUID id) {
        Optional<ProductEntity> byId = mongoDbProductRepository.findById(id);
        return byId.map(this::mapProductEntityToProduct);
    }

}
