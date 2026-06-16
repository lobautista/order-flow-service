package com.bautista.order_flow_service.order.application;

import com.bautista.order_flow_service.order.application.port.in.CreateOrderUseCase;
import com.bautista.order_flow_service.order.application.port.out.IdempotencyPort;
import com.bautista.order_flow_service.order.domain.Order;
import com.bautista.order_flow_service.order.domain.OrderItem;
import com.bautista.order_flow_service.order.domain.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreateOrders implements CreateOrderUseCase {
    private OrderRepository orderRepository;
    private IdempotencyPort idempotencyPort;
    public CreateOrders(OrderRepository orderRepository, IdempotencyPort idempotencyPort) {
        this.orderRepository = orderRepository;
        this.idempotencyPort = idempotencyPort;
    }

    @Transactional
    @Override
    public Order execute(UUID customerId, String currency, List<OrderItem> orderItems, UUID idempotencyKey) {
        Optional<Order> cached = idempotencyPort.findCachedResponse(idempotencyKey, "/orders");
        if (cached.isPresent()) return cached.get();
        Order order = Order.create(customerId, currency, orderItems);
        order = this.orderRepository.save(order);
        idempotencyPort.save(idempotencyKey, "/orders", HttpStatus.CREATED.value(), order);
        return order;
    }
}
