package com.bautista.order_flow_service.order.infrastructure.web;

import com.bautista.order_flow_service.order.domain.Order;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID orderId;
    private String status;
    private BigDecimal totalAmount;
    private String currency;
    private Instant createdAt;

    public static OrderResponse from(Order order) {
        OrderResponse response = new OrderResponse();
        response.orderId = order.getOrderId();
        response.status = order.getStatus().name();
        response.totalAmount = order.getTotalAmount();
        response.currency = order.getCurrency();
        response.createdAt = order.getCreatedAt();
        return response;
    }
}
