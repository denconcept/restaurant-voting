package com.denconcept.restaurantvoting.app.config;

import com.denconcept.restaurantvoting.common.error.AppException;
import com.denconcept.restaurantvoting.common.error.ErrorType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(AppException ex, HttpServletRequest request) {
        log.warn("Business error: {}", ex.getMessage());
        ErrorType errorType = ex.getErrorType();
        ProblemDetail problem = createProblemDetail(errorType, ex.getMessage(), request);
        return ResponseEntity.status(errorType.getStatus()).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("Request validation failed: {}", errors);
        ErrorType errorType = ErrorType.INVALID_DATA;
        ProblemDetail problem = createProblemDetail(errorType, "Request validation failed", request);
        problem.setProperty("errors", errors);
        return ResponseEntity.status(errorType.getStatus()).body(problem);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        log.warn("Data integrity violation", ex);
        ErrorType errorType = ErrorType.DATA_CONFLICT;
        ProblemDetail problem = createProblemDetail(errorType, "Data conflict", request);
        return ResponseEntity.status(errorType.getStatus()).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected server error", ex);
        ErrorType errorType = ErrorType.APP_ERROR;
        ProblemDetail problem = createProblemDetail(errorType, "Unexpected server error", request);
        return ResponseEntity.status(errorType.getStatus()).body(problem);
    }

    private ProblemDetail createProblemDetail(ErrorType errorType, String detail, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(errorType.getStatus());
        problem.setTitle(errorType.getTitle());
        problem.setDetail(detail);
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }
}