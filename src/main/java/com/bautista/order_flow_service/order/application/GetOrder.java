package com.bautista.order_flow_service.order.application;

import com.bautista.order_flow_service.order.application.port.in.GetOrderUseCase;
import com.bautista.order_flow_service.order.domain.Order;
import com.bautista.order_flow_service.order.domain.OrderRepository;
import com.bautista.order_flow_service.order.domain.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetOrder implements GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Order find(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
