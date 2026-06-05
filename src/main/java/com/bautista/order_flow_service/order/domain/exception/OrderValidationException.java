package com.bautista.order_flow_service.order.domain.exception;

public class OrderValidationException extends RuntimeException{
    public  OrderValidationException (String message) {
        super(message);
    }
}
