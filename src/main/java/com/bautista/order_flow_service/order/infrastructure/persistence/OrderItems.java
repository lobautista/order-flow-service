package com.bautista.order_flow_service.order.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrderJpaEntity order;
}
