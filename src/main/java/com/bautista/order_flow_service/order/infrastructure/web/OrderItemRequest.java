package com.bautista.order_flow_service.order.infrastructure.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemRequest {
    @NotNull(message = "Missing productId")
    private UUID productId;

    @Positive(message = "Enter a quantity greater than 0")
    private int quantity;

    @NotNull(message = "Missing unitPrice")
    @Positive(message = "Enter a unitPrice greater than 0")
    private BigDecimal unitPrice;
}
