package dev.java.ecommerce.basket_service.exception;

public class EntityNotFoundError extends RuntimeException{
    public EntityNotFoundError(String message){
        super(message);
    }
}
