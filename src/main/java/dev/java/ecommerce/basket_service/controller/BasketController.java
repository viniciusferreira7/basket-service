package dev.java.ecommerce.basket_service.controller;

import dev.java.ecommerce.basket_service.Service.BasketService;
import dev.java.ecommerce.basket_service.controller.request.BasketRequest;
import dev.java.ecommerce.basket_service.entity.Basket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
@Tag(name = "Basket", description = "Basket management APIs")
public class BasketController {
    private final BasketService basketService;

    @Operation(
            summary = "Create a new basket",
            description = "Creates a new basket for a client with the specified products"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Successfully created basket",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Basket.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<Basket> createBasket(@RequestBody BasketRequest basketRequest){
        Basket basket = basketService.createBasket(basketRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(basket);
    }

    @Operation(
            summary = "Get basket by ID",
            description = "Retrieves a basket by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved basket",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Basket.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Basket not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Basket>> getById(@PathVariable String id){
        Optional<Basket> basketOptional = basketService.getById(id);

        return basketOptional.map(basket -> ResponseEntity.status(HttpStatus.OK)
                .body(Optional.of(basket)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }
}
