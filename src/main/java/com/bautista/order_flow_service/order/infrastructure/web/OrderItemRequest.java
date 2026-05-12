package com.bautista.order_flow_service.order.infrastructure.web;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemRequest {
    private UUID productId;
    private int quantity;
    private double unitPrice;
}
