package com.denconcept.restaurantvoting.app.config;

import com.denconcept.restaurantvoting.common.error.ErrorType;
import com.denconcept.restaurantvoting.common.error.IllegalRequestDataException;
import com.denconcept.restaurantvoting.common.error.NotFoundException;
import com.denconcept.restaurantvoting.common.error.VotingDeadlineExceededException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Map<Class<? extends Throwable>, ErrorType> HTTP_STATUS_MAP =
            Map.of(
                    IllegalRequestDataException.class, ErrorType.INVALID_DATA,
                    NotFoundException.class, ErrorType.NOT_FOUND,
                    DataIntegrityViolationException.class, ErrorType.DATA_CONFLICT,
                    VotingDeadlineExceededException.class, ErrorType.INVALID_DATA
            );

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ErrorType errorType = ErrorType.INVALID_DATA;
        ProblemDetail problem = ProblemDetail.forStatus(errorType.getStatus());
        problem.setTitle(errorType.getTitle());
        problem.setDetail("Request validation failed");
        problem.setProperty("errors", errors);
        return ResponseEntity
                .status(errorType.getStatus())
                .body(problem);
    }

    @ExceptionHandler({
            IllegalRequestDataException.class,
            NotFoundException.class,
            DataIntegrityViolationException.class,
            VotingDeadlineExceededException.class
    })
    public ResponseEntity<ProblemDetail> handleBusinessException(Exception ex) {
        ErrorType errorType = HTTP_STATUS_MAP.getOrDefault(ex.getClass(), ErrorType.APP_ERROR);
        ProblemDetail problem = ProblemDetail.forStatus(errorType.getStatus());
        problem.setTitle(errorType.getTitle());
        problem.setDetail(ex.getMessage());
        return ResponseEntity
                .status(errorType.getStatus())
                .body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(Exception ex) {
        ErrorType errorType = ErrorType.APP_ERROR;
        ProblemDetail problem = ProblemDetail.forStatus(errorType.getStatus());
        problem.setTitle(errorType.getTitle());
        problem.setDetail("Unexpected server error");
        return ResponseEntity
                .status(errorType.getStatus())
                .body(problem);
    }
}