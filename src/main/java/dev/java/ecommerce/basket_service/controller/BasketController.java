package dev.java.ecommerce.basket_service.controller;

import dev.java.ecommerce.basket_service.Service.BasketService;
import dev.java.ecommerce.basket_service.controller.request.BasketRequest;
import dev.java.ecommerce.basket_service.controller.request.PaymentRequest;
import dev.java.ecommerce.basket_service.entity.Basket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public ResponseEntity<Basket> createBasket(@Valid @RequestBody BasketRequest basketRequest){
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
    public ResponseEntity<Basket> getById(@PathVariable String id){
        Basket basket = basketService.getById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(basket);

    }

    @Operation(
            summary = "Update an existing basket",
            description = "Updates a basket with new products and information. Returns 204 No Content on success"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully updated basket",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body - missing required fields (clientId, productsRequest) or invalid data format",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Basket not found - the specified basket ID does not exist",
                    content = @Content
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateBasket(
            @PathVariable String id,
            @RequestBody
            BasketRequest basketRequest){


        Optional<Basket> basketOptional = basketService.update(id, basketRequest);

        return basketOptional.<ResponseEntity<Void>>map(basket -> ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .build())
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Operation(
            summary = "Pay basket",
            description = "Processes payment for a basket using the specified payment method"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully processed payment",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid payment method or basket already sold",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Basket not found",
                    content = @Content
            )
    })
    @PatchMapping("/{id}/payment")
    public ResponseEntity<Void> payBasket(
            @PathVariable String id,
           @Valid @RequestBody
            PaymentRequest paymentRequest){
        basketService.payBasket(id,  paymentRequest);

        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Delete basket by ID",
            description = "Deletes a basket by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted basket",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Basket not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBasketById(@PathVariable String id){
       basketService.deleteById(id);


        return ResponseEntity.noContent().build();
    }
}
