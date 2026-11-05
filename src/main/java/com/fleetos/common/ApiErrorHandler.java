package com.fleetos.common;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
@RestControllerAdvice
public class ApiErrorHandler {
  @ExceptionHandler(ApiException.class)
  ResponseEntity<Map<String,Object>> api(ApiException e) { return response(e.getStatus(), e.getMessage()); }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getFieldErrors().stream().findFirst()
        .map(err -> err.getField() + ": " + err.getDefaultMessage()).orElse("Invalid request");
    return response(HttpStatus.BAD_REQUEST, message);
  }
  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<Map<String,Object>> conflict(DataIntegrityViolationException e) {
    return response(HttpStatus.CONFLICT, "Duplicate or invalid related record");
  }
  private ResponseEntity<Map<String,Object>> response(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(Map.of("timestamp", Instant.now().toString(), "status", status.value(), "error", message));
  }
}
