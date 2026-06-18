package com.bautista.order_flow_service.shared;

import com.bautista.order_flow_service.order.domain.exception.OrderNotFoundException;
import com.bautista.order_flow_service.order.domain.exception.OrderValidationException;
import com.bautista.order_flow_service.shared.infrastructure.web.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleValidationErrors (MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));
        ErrorResponse response = ErrorResponse.of(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleOrderValidationException(OrderValidationException ex) {
        ErrorResponse response = ErrorResponse.of(List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
        ErrorResponse response = ErrorResponse.of(List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMissingRequestHeader(MissingRequestHeaderException ex) {
        ErrorResponse response = ErrorResponse.of(List.of("Missing required header: " + ex.getHeaderName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
