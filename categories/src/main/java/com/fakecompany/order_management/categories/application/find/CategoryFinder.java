package com.fakecompany.order_management.categories.application.find;

import com.fakecompany.order_management.categories.domain.Category;
import com.fakecompany.order_management.categories.domain.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryFinder {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

}
