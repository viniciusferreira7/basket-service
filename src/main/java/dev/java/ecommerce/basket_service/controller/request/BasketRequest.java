package dev.java.ecommerce.basket_service.controller.request;

import dev.java.ecommerce.basket_service.entity.Product;
import lombok.Builder;

import java.util.List;

@Builder
public record BasketRequest(Long clientId, List<ProductRequest> productsRequest) {
}
