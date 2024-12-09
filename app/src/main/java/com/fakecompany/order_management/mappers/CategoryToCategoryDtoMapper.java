package com.fakecompany.order_management.mappers;

import com.fakecompany.order_management.api.dto.CategoryDto;
import com.fakecompany.order_management.categories.domain.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryDtoMapper {

    public CategoryDto map(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto categoryDto = new CategoryDto(category.getId(), category.getName());
        categoryDto.setParent(category.getParent() != null ? map(category.getParent()) : null);
        return categoryDto;
    }

}
