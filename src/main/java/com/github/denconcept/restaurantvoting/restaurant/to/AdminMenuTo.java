package com.github.denconcept.restaurantvoting.restaurant.to;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AdminMenuTo(@NotNull Integer id, @NotNull LocalDate menuDate, @NotNull Integer restaurantId) {
}