package dev.java.ecommerce.basket_service.Service;

import dev.java.ecommerce.basket_service.client.PlatziStoreClient;
import dev.java.ecommerce.basket_service.client.response.PlatziProductResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final PlatziStoreClient platziStoreClient;

    @Cacheable(value = "products")
    public List<PlatziProductResponse> getAllProducts(){
        return platziStoreClient.getAllProducts();
    }

    @Cacheable(value = "product", key = "#productId")
    public Optional<PlatziProductResponse> getProductById(Long productId){
        return Optional.ofNullable(platziStoreClient.getProductId(productId));
    }
}
