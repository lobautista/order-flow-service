package com.bautista.order_flow_service.order.domain;

import com.bautista.order_flow_service.order.domain.exception.OrderValidationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private final List<OrderStatusHistory> orderStatusHistory;

    @JsonCreator
    private Order(
            @JsonProperty("orderId") UUID orderId,
            @JsonProperty("customerId") UUID customerId,
            @JsonProperty("currency") String currency,
            @JsonProperty("items") List<OrderItem> items,
            @JsonProperty("createdAt") Instant createdAt,
            @JsonProperty("orderStatusHistory") List<OrderStatusHistory> orderStatusHistory) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.currency = currency;
        this.items = items;
        this.status = OrderStatus.PENDING;
        this.totalAmount = items.stream().map(OrderItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.createdAt = createdAt;
        this.orderStatusHistory = orderStatusHistory;
    }

    public static Order create(UUID customerId, String currency, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new OrderValidationException("Order must contain at least one item");
        }
        UUID orderId = UuidCreator.getTimeOrdered();
        Instant now = Instant.now();
        List<OrderStatusHistory> orderStatusHistory = List.of(new OrderStatusHistory(
                orderId,
                null,
                OrderStatus.PENDING,
                "Order created",
                null,
                now
        ));
        return new Order(orderId, customerId, currency, items, now, orderStatusHistory);
    }
}
