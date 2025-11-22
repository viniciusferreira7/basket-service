package dev.java.ecommerce.basket_service.controller.request;

import dev.java.ecommerce.basket_service.entity.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "PaymentRequest",
        description = "Request object for basket payment",
        example = "{\"payMethod\": \"PIX\"}"
)
public class PaymentRequest {

    @NotNull(message = "payMethod is required")
    @Schema(
            description = "Payment method: PIX, CREDIT, or DEBIT",
            example = "PIX"
    )
    private PaymentMethod payMethod;
}
