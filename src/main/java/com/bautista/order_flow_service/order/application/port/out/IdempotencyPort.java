package com.bautista.order_flow_service.order.application.port.out;

import com.bautista.order_flow_service.order.domain.Order;

import java.util.Optional;
import java.util.UUID;

public interface IdempotencyPort {
    Optional<Order> findCachedResponse(UUID key, String endpoint);
    void save(UUID key, String endpoint, int httpStatus, Order response);
}
