package com.bautista.order_flow_service.order.application;

import com.bautista.order_flow_service.order.infrastructure.entities.Order;
import com.bautista.order_flow_service.order.infrastructure.repository.OrdersRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateOrders {
    private final OrdersRepository ordersRepository;

    public CreateOrders (OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public Order execute() {
        //UUID id = UuidCreator.getTimeOrdered();
        //UUID customerId = UuidCreator.getTimeOrdered();
        //Order order = new Order(id, customerId, "created");
        return this.ordersRepository.save(null);
    }
}
