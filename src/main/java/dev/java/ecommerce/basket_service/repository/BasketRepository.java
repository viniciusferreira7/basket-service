package dev.java.ecommerce.basket_service.repository;

import dev.java.ecommerce.basket_service.entity.Basket;
import dev.java.ecommerce.basket_service.entity.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BasketRepository extends MongoRepository<Basket, String> {
    List<Basket> findByClientAndStatus(Long client, Status status);
}
