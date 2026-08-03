package com.github.denconcept.restaurantvoting.vote.to;

import java.time.LocalDate;

public record VoteTo(LocalDate voteDate, Integer restaurantId, String restaurantName) {
}