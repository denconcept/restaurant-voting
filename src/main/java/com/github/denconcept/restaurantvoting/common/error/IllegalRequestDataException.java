package com.github.denconcept.restaurantvoting.common.error;

public class IllegalRequestDataException extends AppException {

    public IllegalRequestDataException(String message) {
        super(message, ErrorType.INVALID_DATA);
    }
}
