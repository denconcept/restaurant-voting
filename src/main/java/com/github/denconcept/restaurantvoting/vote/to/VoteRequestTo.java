package com.github.denconcept.restaurantvoting.vote.to;

import jakarta.validation.constraints.NotNull;

public record VoteRequestTo(@NotNull Integer restaurantId) {
}