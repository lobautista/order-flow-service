package com.bautista.order_flow_service.order.infrastructure.persistence;

import com.bautista.order_flow_service.order.domain.Order;
import com.bautista.order_flow_service.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderPersistenceAdapter implements OrderRepository {

    private OrderJpaRepository orderJpaRepository;

    public OrderPersistenceAdapter (OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = toEntity(order);
        this.orderJpaRepository.save(entity);
        return order;
    }

    private OrderJpaEntity toEntity(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity(
                order.getOrderId(),
                order.getCustomerId(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getCurrency(),
                order.getCreatedAt(),
                Instant.now(),
                null,  // set below
                new ArrayList<>()
        );

        List<OrderItem> jpaItems = order.getItems()
                .stream()
                .map(item -> new OrderItem(
                        item.getOrderItemId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubTotal(),
                        Instant.now(),
                        entity
                )).toList();
        entity.setItems(jpaItems);

        return entity;
    }
}
