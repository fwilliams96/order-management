package com.fakecompany.order_management.products.domain;

import com.fakecompany.order_management.categories.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@ToString
@Builder
public class Product {

    private UUID id;
    private String name;
    private BigDecimal price;
    private ProductImage image;
    private Category category;
    private Integer stock;

    public void reduceStock(Integer units) {
        if (stock != null) {
            stock -= units;
        }
    }

}
