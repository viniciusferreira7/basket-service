package dev.java.ecommerce.basket_service.client;

import dev.java.ecommerce.basket_service.client.response.PlatziProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "PlatziStoreClient", url= "${basket.client.platzi}")
public interface PlatziStoreClient {

    @GetMapping("/products")
    public List<PlatziProductResponse> getAllProducts();

    @GetMapping("/products/{id}")
    public Optional<PlatziProductResponse> getProductId(@PathVariable  Long id);
}
