package com.example.api.infra.exception;

import com.example.api.domain.ValidationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleError400(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors();

        return ResponseEntity.badRequest().body(errors.stream().map(ErrorDataValidation::new).toList());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleBusinessRuleError(ValidationException exception) {
        Map<String, String> jsonResponse = Map.of("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jsonResponse);
//        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    private record ErrorDataValidation(String field, String message) {

        public ErrorDataValidation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }

    }
}
