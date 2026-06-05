package com.bautista.order_flow_service.order.application;

import com.bautista.order_flow_service.order.application.port.in.CreateOrderUseCase;
import com.bautista.order_flow_service.order.domain.Order;
import com.bautista.order_flow_service.order.domain.OrderItem;
import com.bautista.order_flow_service.order.domain.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CreateOrders implements CreateOrderUseCase {
    private OrderRepository orderRepository;
    public CreateOrders(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public Order execute(UUID customerId, String currency, List<OrderItem> orderItems) {
        Order order = Order.create(customerId, currency, orderItems);
        return this.orderRepository.save(order);
    }
}
