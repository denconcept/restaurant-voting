package com.denconcept.restaurantvoting.restaurant.to;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AdminMenuTo(@NotNull LocalDate menuDate, @NotNull Integer restaurantId) {
}