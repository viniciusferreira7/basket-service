package dev.java.ecommerce.basket_service.Service;

import dev.java.ecommerce.basket_service.client.response.PlatziProductResponse;
import dev.java.ecommerce.basket_service.controller.request.BasketRequest;
import dev.java.ecommerce.basket_service.entity.Basket;
import dev.java.ecommerce.basket_service.entity.Product;
import dev.java.ecommerce.basket_service.entity.Status;
import dev.java.ecommerce.basket_service.repository.BasketRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductService productService;

    public Basket createBasket(BasketRequest basketRequest){
        basketRepository.findByClientAndStatus(basketRequest.clientId(), Status.OPEN)
                .ifPresent(basket -> {
            throw  new IllegalArgumentException("There is already an open basket for this client");
        });

       List<Product> products = new ArrayList<>();

       basketRequest.productsRequest().forEach((productRequest) -> {
           Optional<PlatziProductResponse> platziProductResponseOptional = productService.getProductById(productRequest.id());

           if(platziProductResponseOptional.isPresent()){
               PlatziProductResponse productResponse = platziProductResponseOptional.get();

               Product product = Product.builder()
                       .id(productResponse.id())
                       .title(productResponse.title())
                       .price(productResponse.price())
                       .quantity(productRequest.quantity())
                       .build();

               products.add(product);
           }

       });


        Basket basket = Basket.builder()
                .client(basketRequest.clientId())
                .products(products)
                .status(Status.OPEN)
                .build();

        basket.calculateTotalPrice();

        return basketRepository.save(basket);

    }

}
