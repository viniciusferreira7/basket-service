package dev.java.ecommerce.basket_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Product {

    private Long id;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
