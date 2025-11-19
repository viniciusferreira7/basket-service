package dev.java.ecommerce.basket_service.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.java.ecommerce.basket_service.exception.BadRequest;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum PaymentMethod {
    PIX, CREDIT, DEBIT;

    @JsonCreator
    public static PaymentMethod fromString(String value) {
        try {
            return PaymentMethod.valueOf(value.toUpperCase());
        } catch (Exception e) {
            String validValues = Arrays.stream(PaymentMethod.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));

            throw new BadRequest(
                    "Invalid payment method: " + value +
                            ". Valid values: " + validValues
            );
        }
    }
}
