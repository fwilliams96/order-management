package com.fakecompany.order_management.categories.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@Builder
public class Category {

    private UUID id;
    private String name;
    private Category parent;

}
