package dev.java.ecommerce.basket_service.client.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@Schema(description = "Product information from Platzi API")
public record PlatziProductResponse(
        @Schema(description = "Unique identifier of the product", example = "1")
        Long id,

        @Schema(description = "Title/name of the product", example = "Wireless Headphones")
        String title,

        @Schema(description = "Price of the product", example = "99.99")
        BigDecimal price
) {
}
