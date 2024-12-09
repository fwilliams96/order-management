package com.fakecompany.order_management.controllers;

import com.fakecompany.order_management.api.ProductsGetV1Api;
import com.fakecompany.order_management.api.dto.ProductDto;
import com.fakecompany.order_management.api.dto.ProductImageDto;
import com.fakecompany.order_management.mappers.CategoryToCategoryDtoMapper;
import com.fakecompany.order_management.products.application.find.ProductFinder;
import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductsGetV1Controller implements ProductsGetV1Api {

    private final ProductFinder productFinder;
    private final CategoryToCategoryDtoMapper categoryToCategoryDtoMapper;

    @Override
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<Product> all = productFinder.findAll();
        return ResponseEntity.ok(all.stream().map(this::mapProductToDto).toList());
    }

    private ProductDto mapProductToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice() != null ? product.getPrice().doubleValue() : 0d,
                categoryToCategoryDtoMapper.map(product.getCategory()),
                productImageToDto(product.getImage())
        );
    }

    private ProductImageDto productImageToDto(ProductImage productImage) {
        if (productImage == null) {
            return null;
        }
        ProductImageDto productImageDto = new ProductImageDto();
        productImageDto.setUrl(productImage.getUrl());
        return productImageDto;
    }


}
