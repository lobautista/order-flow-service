package com.bautista.order_flow_service.order.domain;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class OrderStatusHistory {
    private UUID historyId;
    private UUID orderId;
    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private String reason;
    private String eventId;
    private Instant changedAt;

    public OrderStatusHistory (UUID orderId, OrderStatus oldStatus, OrderStatus newStatus, String reason, String eventId, Instant changedAt) {
        this.historyId = UuidCreator.getTimeOrdered();
        this.orderId = orderId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.reason = reason;
        this.eventId = eventId;
        this.changedAt = changedAt;
    }
}
