package com.bautista.order_flow_service.order.infrastructure.persistence;

import com.bautista.order_flow_service.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderPersistenceAdapter implements OrderRepository {

    private OrderJpaRepository orderJpaRepository;

    public OrderPersistenceAdapter (OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }
}
