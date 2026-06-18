package com.bautista.order_flow_service.order.infrastructure.persistence;

import com.bautista.order_flow_service.order.domain.Order;
import com.bautista.order_flow_service.order.domain.OrderRepository;
import com.bautista.order_flow_service.order.domain.OrderStatus;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Optional<Order> findById(UUID id) {
        return this.orderJpaRepository.findById(id).map(this::toDomain);
    }

    private Order toDomain(OrderJpaEntity entity) {
        List<com.bautista.order_flow_service.order.domain.OrderItem> items = entity.getItems()
                .stream()
                .map(item -> new com.bautista.order_flow_service.order.domain.OrderItem(
                        item.getOrderItemId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getUnitPrice()
                )).toList();

        List<com.bautista.order_flow_service.order.domain.OrderStatusHistory> statuses = entity.getStatuses()
                .stream()
                .map(history -> new com.bautista.order_flow_service.order.domain.OrderStatusHistory(
                        history.getHistoryId(),
                        entity.getOrderId(),
                        history.getOldStatus() != null ? OrderStatus.valueOf(history.getOldStatus()) : null,
                        OrderStatus.valueOf(history.getNewStatus()),
                        history.getReason(),
                        history.getEventId(),
                        history.getChangedAt()
                )).toList();

        return Order.fromPersistence(
                entity.getOrderId(),
                entity.getCustomerId(),
                OrderStatus.valueOf(entity.getStatus()),
                entity.getTotalAmount(),
                entity.getCurrency(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                items,
                statuses
        );
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
                null
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

        List<OrderStatusHistory> jpaHistory = order.getOrderStatusHistory()
                .stream()
                .map(history -> new OrderStatusHistory(
                        history.getHistoryId(),
                        null != history.getOldStatus() ? history.getOldStatus().name() : null,
                        history.getNewStatus().name(),
                        history.getReason(),
                        history.getEventId(),
                        history.getChangedAt(),
                        entity
                )).toList();

        entity.setItems(jpaItems);
        entity.setStatuses(jpaHistory);

        return entity;
    }
}
