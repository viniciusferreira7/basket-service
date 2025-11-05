package dev.java.ecommerce.basket_service.Service;

import dev.java.ecommerce.basket_service.entity.Basket;
import dev.java.ecommerce.basket_service.repository.BasketRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;

    private Basket createBasket(BasketRequest basketRequest){
        Basket basket = Basket.builder()

                .build();

        basket.calculateTotalPrice();

        return basketRepository.save(basket);

    }

}
