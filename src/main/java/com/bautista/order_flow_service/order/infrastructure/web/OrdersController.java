package com.bautista.order_flow_service.order.infrastructure.web;

import com.bautista.order_flow_service.order.application.port.in.CreateOrderUseCase;
import com.bautista.order_flow_service.order.domain.Order;
import com.bautista.order_flow_service.order.domain.OrderItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrdersController {
    private CreateOrderUseCase createOrderUseCase;

    public OrdersController (CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> postOrders(@RequestBody CreateOrderRequest createOrderRequest) {
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
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
    }
}
