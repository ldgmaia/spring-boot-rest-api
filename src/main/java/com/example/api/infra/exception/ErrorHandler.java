package com.example.api.infra.exception;

import com.example.api.domain.ValidationException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

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

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, String>> handleBusinessRuleError(HttpClientErrorException exception) {
        String defaultMessage = "An error occurred while processing your request.";
        try {
            // Extract the response body
            String responseBody = exception.getResponseBodyAsString();
            JsonNode root = objectMapper.readTree(responseBody);

            // Navigate to the specific error message
            JsonNode faultNode = root.path("Fault").path("Error");
            if (faultNode.isArray() && faultNode.size() > 0) {
                JsonNode firstError = faultNode.get(0);
                String message = firstError.path("Message").asText();
                String detail = firstError.path("Detail").asText();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", message, "detail", detail));
            }
        } catch (IOException e) {
            // Log the error and return a generic message
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", defaultMessage));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String friendlyMessage = getFriendlyMessage(exception);
        Map<String, String> jsonResponse = Map.of("message", friendlyMessage);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResponse);
//        Map<String, String> jsonResponse = Map.of("message", exception.getMessage());
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResponse);
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity handleUniqueConstraintViolationException(UniqueConstraintViolationException exception) {
        Map<String, String> jsonResponse = Map.of("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResponse);
    }

    private String getFriendlyMessage(DataIntegrityViolationException exception) {
        String message = exception.getMessage();

        if (message.contains("Duplicate entry")) {
            String[] parts = message.split("'");
            String entry = parts[1];
            String key = parts[3];
            if (key.contains("sections.models_id")) {
                return "A section with the name '" + entry.split("-")[1] + "' already exists.";
            }
            return "A record with the same unique identifier already exists. Please use a different value.";
        }
        // Add more checks for other common data integrity violations if needed
        return "A data integrity violation occurred. Please check your input and try again.";
    }

    private record ErrorDataValidation(String field, String message) {
        public ErrorDataValidation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
