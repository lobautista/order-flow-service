package com.bautista.order_flow_service.order.infrastructure.persistence;

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
@Table(name = "order_status_history")
public class OrderStatusHistory {

    @Id
    @Column(name = "history_id", nullable = false, updatable = false)
    private UUID historyId;

    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID orderId;

    @Column(name = "old_status", nullable = false, updatable = false)
    private String oldStatus;

    @Column(name = "new_status", nullable = false, updatable = false)
    private String newStatus;

    @Column(name = "reason", nullable = false, updatable = false)
    private String reason;

    @Column(name = "event_id", nullable = false, updatable = false)
    private String eventId;

    @Column(name = "changed_at", nullable = false, updatable = false)
    private Timestamp changedAt;
}
