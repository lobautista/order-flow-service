package com.bautista.order_flow_service.order.infrastructure.persistence;

import com.bautista.order_flow_service.order.application.port.out.IdempotencyPort;
import com.bautista.order_flow_service.order.domain.Order;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class IdempotencyService implements IdempotencyPort {

    private final IdempotencyKeyJpaRepository idempotencyKeyJpaRepository;
    private final ObjectMapper objectMapper;

    public IdempotencyService(IdempotencyKeyJpaRepository idempotencyKeyJpaRepository, ObjectMapper objectMapper) {
        this.idempotencyKeyJpaRepository = idempotencyKeyJpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Order> findCachedResponse(UUID key, String endpoint) {
        return idempotencyKeyJpaRepository.findById(new IdempotencyKeyId(key, endpoint))
                .map(entity -> objectMapper.readValue(entity.getResponseBody(), Order.class));
    }

    @Override
    public void save(UUID key, String endpoint, int httpStatus, Order response) {
        Instant now = Instant.now();
        idempotencyKeyJpaRepository.save(new IdempotencyKeyJpaEntity(
                new IdempotencyKeyId(key, endpoint),
                httpStatus,
                objectMapper.writeValueAsString(response),
                now,
                now.plus(Duration.ofHours(24))
        ));
    }
}
