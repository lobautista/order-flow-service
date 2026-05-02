package com.bautista.order_flow_service.order.application;

import com.bautista.order_flow_service.order.application.port.in.CreateOrderUseCase;
import com.bautista.order_flow_service.order.domain.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateOrders implements CreateOrderUseCase {
    private OrderRepository orderRepository;
    public CreateOrders(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

}
