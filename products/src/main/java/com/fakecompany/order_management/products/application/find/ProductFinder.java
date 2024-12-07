package com.fakecompany.order_management.products.application.find;

import com.fakecompany.order_management.products.domain.Product;
import com.fakecompany.order_management.products.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFinder {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

}
