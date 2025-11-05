package dev.java.ecommerce.basket_service.controller;

import dev.java.ecommerce.basket_service.Service.BasketService;
import dev.java.ecommerce.basket_service.controller.request.BasketRequest;
import dev.java.ecommerce.basket_service.entity.Basket;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @PostMapping
    public ResponseEntity<Basket> createBasket(@RequestBody BasketRequest basketRequest){
        Basket basket = basketService.createBasket(basketRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(basket);
    }
}
