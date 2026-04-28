package com.bautista.order_flow_service.order.infrastructure.repository;

import com.bautista.order_flow_service.order.infrastructure.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrdersRepository extends JpaRepository<Order, UUID> {
    Order save(Order order);

}
