package com.bautista.order_flow_service.order.domain;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderItem {
    private final UUID orderItemId;
    private final UUID productId;
    private final int quantity;
    private final BigDecimal unitPrice;

    public OrderItem(UUID productId, int quantity, BigDecimal unitPrice) {
        this.orderItemId = UuidCreator.getTimeOrdered();
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubTotal() {
        return BigDecimal.valueOf(quantity).multiply(unitPrice);
    }
}
