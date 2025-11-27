package com.events_cav.events_venues.domain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

// Extender ResponseEntityExceptionHandler facilita el manejo de excepciones de Spring como MethodArgumentNotValidException
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Manejo de Excepciones de Negocio Personalizadas (4xx)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                request.getDescription(false)
        );
        logError(problemDetail, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ProblemDetail> handleResourceConflict(ResourceConflictException ex, WebRequest request) {
        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.CONFLICT,
                "Resource Conflict",
                ex.getMessage(),
                request.getDescription(false)
        );
        logError(problemDetail, ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    // Manejo de Excepciones de Validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        // Recopila todos los errores de validación en una lista legible
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                "Input validation failed. Details: " + errors,
                request.getDescription(false)
        );

        //Añadir los errores de campo en el campo de extensiones
        problemDetail.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        org.springframework.validation.FieldError::getField,
                        org.springframework.validation.FieldError::getDefaultMessage
                )));

        logError(problemDetail, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    // Manejo de Excepciones Genéricas (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred. Please contact support.",
                request.getDescription(false)
        );
        logError(problemDetail, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    // Metodo de Soporte para ProblemDetail (RFC 7807)
    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail, String instance) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);

        // Campos estándar ProblemDetail
        problemDetail.setType(URI.create("about:blank")); // Podría ser una URI de documentación de error
        problemDetail.setTitle(title);
        // problemDetail.setInstance(URI.create(instance.substring(4))); // limpia el prefijo 'uri='

        // Extensiones ProblemDetail (para cumplir con timestamp y traceId)
        String traceId = UUID.randomUUID().toString(); // Generación básica de TraceId (mejorado en TASK 2)
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("traceId", traceId);

        return problemDetail;
    }

    private void logError(ProblemDetail problemDetail, Exception ex) {
        // En Task 2, usaremos el log estructurado y traceId
        log.error("TraceId: {} | Status: {} | Title: {} | Detail: {} | Exception: {}",
                problemDetail.getProperties().get("traceId"),
                problemDetail.getStatus(),
                problemDetail.getTitle(),
                problemDetail.getDetail(),
                ex.getClass().getName(),
                ex);
    }
}