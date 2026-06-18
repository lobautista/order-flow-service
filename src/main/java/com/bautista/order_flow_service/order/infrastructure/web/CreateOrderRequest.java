package com.bautista.order_flow_service.order.infrastructure.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderRequest {
    @NotNull(message = "Missing customerId")
    private UUID customerId;

    @Valid
    @NotEmpty(message = "Enter a valid list for orderItemRequests")
    private List<OrderItemRequest> orderItemRequests;

    @NotBlank(message = "Missing currency")
    @Pattern(regexp = "[A-Z]{3}", message = "Enter valid currency value")
    private String currency;
}
