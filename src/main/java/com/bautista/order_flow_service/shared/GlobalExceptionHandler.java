package com.bautista.order_flow_service.shared;

import com.bautista.order_flow_service.shared.infrastructure.web.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
}
