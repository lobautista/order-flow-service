package com.bautista.order_flow_service.shared.infrastructure.web;

import com.bautista.order_flow_service.order.domain.Order;
import com.bautista.order_flow_service.order.infrastructure.web.OrderResponse;
import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private List<String> errors;

    public static ErrorResponse of(List<String> errorsList) {
        ErrorResponse response = new ErrorResponse();
        response.setErrors(errorsList);
        return response;
    }
}
