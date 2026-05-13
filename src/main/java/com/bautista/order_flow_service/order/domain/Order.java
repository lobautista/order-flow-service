package com.bautista.order_flow_service.order.domain;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Order {
    private final UUID orderId;
    private final UUID customerId;
    private final OrderStatus status;
    private final List<OrderItem> items;
    private final String currency;
    private final BigDecimal totalAmount;
    private final Instant createdAt;

    private Order(UUID customerId, String currency, List<OrderItem> items) {
        this.orderId = UuidCreator.getTimeOrdered();
        this.customerId = customerId;
        this.currency = currency;
        this.items = items;
        this.status = OrderStatus.PENDING;
        this.totalAmount = items.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.createdAt = Instant.now();
    }

    public static Order create(UUID customerId, String currency, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        return new Order(customerId, currency, items);
    }
}
