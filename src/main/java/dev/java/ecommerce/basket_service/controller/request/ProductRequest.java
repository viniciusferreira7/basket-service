package dev.java.ecommerce.basket_service.controller.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequest(Long id, Integer quantity) {
}
