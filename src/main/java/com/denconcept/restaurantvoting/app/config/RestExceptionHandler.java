package com.denconcept.restaurantvoting.app.config;

import com.denconcept.restaurantvoting.common.error.ErrorType;
import com.denconcept.restaurantvoting.common.error.IllegalRequestDataException;
import com.denconcept.restaurantvoting.common.error.NotFoundException;
import com.denconcept.restaurantvoting.common.error.VotingDeadlineExceededException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Map<Class<? extends Throwable>, ErrorType> HTTP_STATUS_MAP =
            Map.of(
                    MethodArgumentNotValidException.class, ErrorType.INVALID_DATA,
                    IllegalRequestDataException.class, ErrorType.INVALID_DATA,
                    NotFoundException.class, ErrorType.NOT_FOUND,
                    DataIntegrityViolationException.class, ErrorType.DATA_CONFLICT,
                    VotingDeadlineExceededException.class, ErrorType.INVALID_DATA
            );

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            IllegalRequestDataException.class,
            NotFoundException.class,
            DataIntegrityViolationException.class,
            VotingDeadlineExceededException.class
    })
    public ResponseEntity<String> handleException(Exception ex) {
        ErrorType errorType = HTTP_STATUS_MAP.get(ex.getClass());
        return ResponseEntity
                .status(errorType.getStatus())
                .body(ex.getMessage());
    }
}