package com.bautista.order_flow_service.order.application.port.in;

import com.bautista.order_flow_service.order.domain.Order;
import com.bautista.order_flow_service.order.domain.OrderItem;

import java.util.List;
import java.util.UUID;

public interface CreateOrderUseCase {
    Order execute(UUID customerId, String currency, List<OrderItem> orderItems, UUID idempotencyKey);
}
