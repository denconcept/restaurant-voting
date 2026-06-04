package com.denconcept.restaurantvoting.common.error;

public class VotingDeadlineExceededException extends RuntimeException {

    public VotingDeadlineExceededException(String message) {
        super(message);
    }
}