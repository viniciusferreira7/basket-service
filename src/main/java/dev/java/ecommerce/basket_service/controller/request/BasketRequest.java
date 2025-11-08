package dev.java.ecommerce.basket_service.controller.request;

import dev.java.ecommerce.basket_service.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(
        name = "BasketRequest",
        description = "Request object for creating a new basket",
        example = "{\"clientId\": 1, \"productsRequest\": [{\"id\": 1, \"quantity\": 2}]}"
)
public record BasketRequest(
        @Schema(description = "The unique identifier of the client", example = "1")
        Long clientId,
        @Schema(description = "List of products to add to the basket")
        List<ProductRequest> productsRequest
) {
}
