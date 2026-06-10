package com.bautista.order_flow_service.order.infrastructure.web;

import com.bautista.order_flow_service.order.application.port.in.CreateOrderUseCase;
import com.bautista.order_flow_service.order.domain.Order;
import com.bautista.order_flow_service.order.domain.OrderItem;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class OrdersController {
    private final CreateOrderUseCase createOrderUseCase;
    private final IdempotencyService idempotencyService;

    public OrdersController(CreateOrderUseCase createOrderUseCase, IdempotencyService idempotencyService) {
        this.createOrderUseCase = createOrderUseCase;
        this.idempotencyService = idempotencyService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> postOrders(
            @Valid @RequestBody CreateOrderRequest createOrderRequest,
            @RequestHeader("Idempotency-Key") UUID idempotencyKey) {
        Optional<ResponseEntity<OrderResponse>> cached = idempotencyService.findCachedResponse(idempotencyKey, "/orders");
        if (cached.isPresent()) return cached.get();

        List<OrderItem> items = createOrderRequest.getOrderItemRequests()
                .stream()
                .map(item -> new OrderItem(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getUnitPrice()))
                .toList();
        Order order = this.createOrderUseCase.execute(
                createOrderRequest.getCustomerId(),
                createOrderRequest.getCurrency(),
                items
        );
        OrderResponse orderResponse = OrderResponse.from(order);
        idempotencyService.save(idempotencyKey, "/orders", HttpStatus.CREATED.value(), orderResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
