package com.bautista.order_flow_service.order.infrastructure.web;

import com.bautista.order_flow_service.order.application.port.in.CreateOrderUseCase;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {
    private CreateOrderUseCase createOrderUseCase;

    public OrdersController (CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }
}
