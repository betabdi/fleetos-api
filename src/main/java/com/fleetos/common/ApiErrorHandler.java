package com.fleetos.common;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestControllerAdvice
public class ApiErrorHandler {
  private static final Logger log=LoggerFactory.getLogger(ApiErrorHandler.class);
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
  @ExceptionHandler(HttpMessageNotReadableException.class)
  ResponseEntity<Map<String,Object>> malformed(HttpMessageNotReadableException e) {
    return response(HttpStatus.BAD_REQUEST, "Malformed JSON or unsupported field value");
  }
  @ExceptionHandler(Exception.class)
  ResponseEntity<Map<String,Object>> unexpected(Exception e,HttpServletRequest request) {
    log.error("Unexpected API error for {}",request.getRequestURI(),e);
    return response(HttpStatus.INTERNAL_SERVER_ERROR,"Unexpected server error");
  }
  private ResponseEntity<Map<String,Object>> response(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(Map.of("timestamp", Instant.now().toString(), "status", status.value(), "error", message));
  }
}
