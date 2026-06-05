package com.bautista.order_flow_service.order.domain;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class OrderStatusHistory {
    private final UUID historyId;
    private final UUID orderId;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;
    private final String reason;
    private final String eventId;
    private final Instant changedAt;

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
