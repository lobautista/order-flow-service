package com.bautista.order_flow_service.order.infrastructure.web;

import com.bautista.order_flow_service.order.infrastructure.persistence.IdempotencyKeyId;
import com.bautista.order_flow_service.order.infrastructure.persistence.IdempotencyKeyJpaEntity;
import com.bautista.order_flow_service.order.infrastructure.persistence.IdempotencyKeyJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class IdempotencyService {

    private final IdempotencyKeyJpaRepository idempotencyKeyJpaRepository;
    private final ObjectMapper objectMapper;

    public IdempotencyService(IdempotencyKeyJpaRepository idempotencyKeyJpaRepository, ObjectMapper objectMapper) {
        this.idempotencyKeyJpaRepository = idempotencyKeyJpaRepository;
        this.objectMapper = objectMapper;
    }

    public Optional<ResponseEntity<OrderResponse>> findCachedResponse(UUID key, String endpoint) {
        return idempotencyKeyJpaRepository.findById(new IdempotencyKeyId(key, endpoint))
                .map(entity -> ResponseEntity
                        .status(entity.getResponseStatus())
                        .body(objectMapper.readValue(entity.getResponseBody(), OrderResponse.class)));
    }

    public void save(UUID key, String endpoint, int httpStatus, OrderResponse response) {
        idempotencyKeyJpaRepository.save(new IdempotencyKeyJpaEntity(
                new IdempotencyKeyId(key, endpoint),
                httpStatus,
                objectMapper.writeValueAsString(response),
                Instant.now(),
                Instant.now().plus(Duration.ofHours(24))
        ));
    }
}
