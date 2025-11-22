package dev.java.ecommerce.basket_service.Service;

import dev.java.ecommerce.basket_service.controller.request.BasketRequest;
import dev.java.ecommerce.basket_service.controller.request.PaymentRequest;
import dev.java.ecommerce.basket_service.entity.Basket;
import dev.java.ecommerce.basket_service.entity.Product;
import dev.java.ecommerce.basket_service.entity.Status;
import dev.java.ecommerce.basket_service.exception.BadRequest;
import dev.java.ecommerce.basket_service.exception.EntityNotFound;
import dev.java.ecommerce.basket_service.repository.BasketRepository;
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

    private Basket convertBasketRequestToBasketEntity(BasketRequest basketRequest){

        if (basketRequest.productsRequest() == null) {
            throw new BadRequest("productsRequest cannot be null");
        }

        List<Product> products = new ArrayList<>();

        basketRequest.productsRequest().forEach(productRequest -> {

            var productResponse = productService
                    .getProductById(productRequest.id())
                    .orElseThrow(() -> new BadRequest(
                            "Product not found: " + productRequest.id()
                    ));

            Product product = Product.builder()
                    .id(productResponse.id())
                    .title(productResponse.title())
                    .price(productResponse.price())
                    .quantity(productRequest.quantity())
                    .build();

            products.add(product);
        });


        Basket basket = Basket.builder()
                .client(basketRequest.clientId())
                .products(products)
                .status(Status.OPEN)
                .build();

        basket.calculateTotalPrice();

        return basket;
    }


    public Basket createBasket(BasketRequest basketRequest){
        List<Basket> baskets = basketRepository.findByClientAndStatus(
                basketRequest.clientId(),
                Status.OPEN
        );

        if (!baskets.isEmpty()) {
            throw new IllegalArgumentException("There is already an open basket for this client");
        }

        Basket basket = convertBasketRequestToBasketEntity(basketRequest);

        return basketRepository.save(basket);
    }


    public Basket getById(String id){
        return basketRepository.findById(id).orElseThrow(() -> new EntityNotFound("Basket not found"));
    }

    public Optional<Basket> update(String id, BasketRequest basketRequest){
        Optional<Basket> basketOptional = basketRepository.findById(id);

        if(basketOptional.isPresent()){
            Basket basket = this.convertBasketRequestToBasketEntity(basketRequest);

            basket.setId(id);

            return Optional.of(basketRepository.save(basket));
        }

        throw new EntityNotFound("Basket not found");

    }

    public void payBasket(String id, PaymentRequest paymentRequest){
        if (paymentRequest == null || paymentRequest.getPayMethod() == null) {
            throw new BadRequest("Payment method is required");
        }

        Basket basket = this.getById(id);

        if(basket.getStatus() == Status.SOLD){
            throw new BadRequest("Basket already sold");
        }

        basket.setPayMethod(paymentRequest.getPayMethod());
        basket.setStatus(Status.SOLD);

        basketRepository.save(basket);
    }
    
    public void deleteById(String id){
        Basket basket = this.getById(id);

        basketRepository.deleteById(id);
    }

}
