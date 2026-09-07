package com.github.denconcept.restaurantvoting.restaurant.to;

public record AdminResponseVotingResultTo(
        Integer restaurantId,
        String name,
        Long voteCount) {
}