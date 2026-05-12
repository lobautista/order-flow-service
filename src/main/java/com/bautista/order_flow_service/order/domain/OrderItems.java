package com.bautista.order_flow_service.order.domain;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OrderItems {
    private final UUID orderItemId;
    private final UUID productId;
    private final int quantity;
    private final double unitPrice;

    public OrderItems (UUID productId, int quantity, double unitPrice) {
        this.orderItemId = UuidCreator.getTimeOrdered();
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public double getSubTotal() {
        return quantity * unitPrice;
    }
}
