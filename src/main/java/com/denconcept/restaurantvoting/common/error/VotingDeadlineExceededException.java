package com.denconcept.restaurantvoting.common.error;

public class VotingDeadlineExceededException extends AppException {

    public VotingDeadlineExceededException(String message) {
        super(message, ErrorType.INVALID_DATA);
    }
}