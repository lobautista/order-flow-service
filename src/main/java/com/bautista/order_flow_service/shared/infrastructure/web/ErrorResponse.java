package com.bautista.order_flow_service.shared.infrastructure.web;

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
