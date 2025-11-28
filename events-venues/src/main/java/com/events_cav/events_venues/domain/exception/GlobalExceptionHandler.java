package com.events_cav.events_venues.domain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC; // Importación para acceder al TraceId del hilo
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

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

    // Manejo de Excepciones de Validación (@Valid) - Error 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                "Input validation failed. Details: " + errors,
                request.getDescription(false)
        );

        problemDetail.setProperty("errors", ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        org.springframework.validation.FieldError::getField,
                        org.springframework.validation.FieldError::getDefaultMessage
                )));

        // Usamos log.warn para errores de validación (400) que son problemas del cliente
        logError(problemDetail, ex, log::warn);
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
        // Usamos log.error para errores internos del servidor (500)
        logError(problemDetail, ex, log::error);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    // Metodo de Soporte para ProblemDetail (RFC 7807)
    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail, String instance) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);

        // Campos estándar ProblemDetail
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setTitle(title);
        // Se puede limpiar el prefijo 'uri=' si es necesario: instance.substring(4)
        problemDetail.setInstance(URI.create(instance));

        // Implementación de correlación: Obtener el traceId del MDC
        // Si Micrometer Tracing está activo, "traceId" se establece automáticamente.
        String traceId = MDC.get("traceId");

        // Extensiones ProblemDetail (para cumplir con timestamp y traceId)
        problemDetail.setProperty("timestamp", Instant.now());
        // Incluimos el traceId obtenido del MDC. Si es null, usamos el de tu logError (N/A)
        problemDetail.setProperty("traceId", traceId != null ? traceId : "N/A");

        return problemDetail;
    }

    // Nuevo metodo de logging que permite especificar el nivel (error, warn)
    private void logError(ProblemDetail problemDetail, Exception ex, java.util.function.BiConsumer<String, Object[]> logger) {

        String logMessage = "TraceId: {} | Status: {} | Tipo de Error: {} | Endpoint: {} | Detalle: {}";

        logger.accept(logMessage, new Object[] {
                problemDetail.getProperties().get("traceId"),
                problemDetail.getStatus(),
                ex.getClass().getSimpleName(), // Tipo de error
                problemDetail.getInstance(),   // Endpoint afectado
                problemDetail.getDetail(),
                ex // Pasa la excepción al final para imprimir el stack trace
        });
    }

    // Mantenemos el metodo original por si quieres seguir usándolo para ResourceNotFound/Conflict
    // Lo he modificado para usar el nuevo logError con nivel ERROR por defecto
    private void logError(ProblemDetail problemDetail, Exception ex) {
        logError(problemDetail, ex, log::error);
    }
}