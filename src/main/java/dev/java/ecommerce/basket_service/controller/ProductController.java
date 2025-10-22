package dev.java.ecommerce.basket_service.controller;

import dev.java.ecommerce.basket_service.Service.ProductService;
import dev.java.ecommerce.basket_service.client.response.PlatziProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Get all products",
            description = "Retrieves a list of all available products from the catalog"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved list of products",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlatziProductResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<PlatziProductResponse> > getAllProducts(){
        List<PlatziProductResponse> platziProductsResponseList = productService.getAllProducts();

        return ResponseEntity.ok(platziProductsResponseList);
    }

    @Operation(
            summary = "Get product by ID",
            description = "Retrieves a specific product by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved product",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PlatziProductResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlatziProductResponse> getProductById(
            @Parameter(description = "ID of the product to retrieve", required = true)
            @PathVariable Long id){
        Optional<PlatziProductResponse> platziProductResponseOptional = productService.getProductById(id);

        return platziProductResponseOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
