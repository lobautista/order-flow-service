package com.bautista.order_flow_service.order.infrastructure.web;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemRequest {
    private UUID productId;
    private int quantity;
    private BigDecimal unitPrice;
}
