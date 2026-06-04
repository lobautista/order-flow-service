package com.bautista.order_flow_service.order.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {

    @Id
    @Column(name = "history_id", nullable = false, updatable = false)
    private UUID historyId;

    @Column(name = "old_status", updatable = false)
    private String oldStatus;

    @Column(name = "new_status", nullable = false, updatable = false)
    private String newStatus;

    @Column(name = "reason", nullable = false, updatable = false)
    private String reason;

    @Column(name = "event_id", updatable = false)
    private String eventId;

    @Column(name = "changed_at", nullable = false, updatable = false)
    private Instant changedAt;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private OrderJpaEntity order;
}
