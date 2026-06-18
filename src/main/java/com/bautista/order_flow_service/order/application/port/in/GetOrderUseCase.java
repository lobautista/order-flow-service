package com.bautista.order_flow_service.order.application.port.in;

import com.bautista.order_flow_service.order.domain.Order;

import java.util.UUID;

public interface GetOrderUseCase {
    Order find(UUID id);
}
