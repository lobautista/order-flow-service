package com.bautista.order_flow_service.order.infrastructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORDER_ITEMS")
public class OrderItems {

    @Id
    @Column(name = "order_item_id", nullable = false, updatable = false)
    private UUID orderItemId;

    @Column(name = "order_Id", nullable = false, updatable = false)
    private UUID orderId;

    @Column(name = "product_id", nullable = false, updatable = false)
    private UUID productId;

    @Column(name = "quantity", nullable = false, updatable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false, updatable = false)
    double unitPrice;

    @Column(name = "subtotal", nullable = false, updatable = false)
    double subtotal;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;
}
