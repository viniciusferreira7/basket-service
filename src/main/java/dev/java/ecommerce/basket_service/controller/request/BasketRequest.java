package dev.java.ecommerce.basket_service.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(
        name = "BasketRequest",
        description = "Request object for creating a new basket",
        example = "{\"clientId\": 1, \"productsRequest\": [{\"id\": 1, \"quantity\": 2}]}"
)
public record BasketRequest(

        @NotNull(message = "clientId is required")
        @Schema(description = "The unique identifier of the client", example = "1")
        Long clientId,

        @NotEmpty(message = "productsRequest cannot be empty")
        @Valid
        @Schema(description = "List of products to add to the basket")
        List<ProductRequest> productsRequest

) { }
