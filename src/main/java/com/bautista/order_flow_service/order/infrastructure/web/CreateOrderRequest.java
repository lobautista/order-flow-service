package com.bautista.order_flow_service.order.infrastructure.web;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderRequest {
    private UUID customerId;
    private List<OrderItemRequest> orderItemRequests;
    private String currency;
}
