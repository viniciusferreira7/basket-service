package dev.java.ecommerce.basket_service.controller;

import dev.java.ecommerce.basket_service.Service.ProductService;
import dev.java.ecommerce.basket_service.client.response.PlatziProductResponse;
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
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<PlatziProductResponse> > getAllProducts(){
        List<PlatziProductResponse> platziProductsResponseList = productService.getAllProducts();

        return ResponseEntity.ok(platziProductsResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatziProductResponse> getProductById(@PathVariable Long id){
        Optional<PlatziProductResponse> platziProductResponseOptional = productService.getProductById(id);

        return platziProductResponseOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
