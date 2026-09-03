package com.github.denconcept.restaurantvoting.restaurant.to;

public record AdminResponseVotingResultTo(
        Integer restaurantId,
        String name,
        Integer voteCount) {
}