package dev.java.ecommerce.basket_service.repository;

import dev.java.ecommerce.basket_service.entity.Basket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BasketRepository extends MongoRepository<Basket, String> {
}
