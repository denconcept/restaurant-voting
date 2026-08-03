package com.github.denconcept.restaurantvoting.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    APP_ERROR("Application error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_DATA("Wrong data", HttpStatus.UNPROCESSABLE_CONTENT),
    NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND),
    DATA_CONFLICT("DataBase conflict", HttpStatus.CONFLICT);

    public final String title;
    public final HttpStatus status;

    ErrorType(String title, HttpStatus status) {
        this.title = title;
        this.status = status;
    }
}