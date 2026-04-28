package com.bautista.order_flow_service.order.api;

import com.bautista.order_flow_service.order.application.CreateOrders;
import com.bautista.order_flow_service.order.infrastructure.entities.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {
    private CreateOrders createOrders;

    public OrdersController (CreateOrders createOrders) {
        this.createOrders = createOrders;
    }

    @PostMapping("/createOrder")
    public Order postOrder () {
        return this.createOrders.execute();
    }
}
