package com.denconcept.restaurantvoting.common.error;

public class NotFoundException extends AppException {

    public NotFoundException(String message) {
        super(message, ErrorType.NOT_FOUND);
    }
}