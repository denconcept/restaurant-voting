package com.denconcept.restaurantvoting.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    INVALID_DATA(HttpStatus.UNPROCESSABLE_CONTENT),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    DATA_CONFLICT(HttpStatus.CONFLICT);

    public final HttpStatus status;

    ErrorType(HttpStatus status) {
        this.status = status;
    }
}