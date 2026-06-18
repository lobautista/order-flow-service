package com.bautista.order_flow_service.order.infrastructure.web;

import com.bautista.order_flow_service.order.application.port.in.CreateOrderUseCase;
import com.bautista.order_flow_service.order.application.port.in.GetOrderUseCase;
import com.bautista.order_flow_service.order.domain.Order;
import com.bautista.order_flow_service.order.domain.OrderItem;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class OrdersController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    public OrdersController(CreateOrderUseCase createOrderUseCase, GetOrderUseCase getOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> postOrders(
            @Valid @RequestBody CreateOrderRequest createOrderRequest,
            @RequestHeader("Idempotency-Key") UUID idempotencyKey) {
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
                items,
                idempotencyKey
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable("id") UUID id) {
        Order order = this.getOrderUseCase.find(id);
        return ResponseEntity.ok(OrderResponse.from(order));
    }
}
