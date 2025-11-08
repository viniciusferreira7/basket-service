package dev.java.ecommerce.basket_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Product {

    private Long id;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
