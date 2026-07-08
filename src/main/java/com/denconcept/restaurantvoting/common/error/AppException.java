package com.denconcept.restaurantvoting.common.error;

import lombok.Getter;
import org.jspecify.annotations.NonNull;

@Getter
public class AppException extends RuntimeException {

    private final ErrorType errorType;

    public AppException(@NonNull String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
}
