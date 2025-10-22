package dev.java.ecommerce.basket_service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "PlatziStoreClient", url= "${basket.client.platzi}")
public interface PlatziStoreClient {

    public void getAllProducts();
    public void getProductId(Long id);
}
