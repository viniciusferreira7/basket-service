package dev.java.ecommerce.basket_service.client.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PlatziProductResponse(Long id, String title, BigDecimal price) {
}
