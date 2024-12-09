package com.fakecompany.order_management.controllers;

import com.fakecompany.order_management.api.CategoriesGetV1Api;
import com.fakecompany.order_management.api.dto.CategoryDto;
import com.fakecompany.order_management.categories.application.find.CategoryFinder;
import com.fakecompany.order_management.categories.domain.Category;
import com.fakecompany.order_management.mappers.CategoryToCategoryDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoriesGetV1Controller implements CategoriesGetV1Api {

    private final CategoryFinder categoryFinder;
    private final CategoryToCategoryDtoMapper categoryToCategoryDtoMapper;

    @Override
    public ResponseEntity<List<CategoryDto>> getCategories() {
        List<Category> all = categoryFinder.findAll();
        return ResponseEntity.ok(all.stream().map(categoryToCategoryDtoMapper::map).toList());
    }

}
