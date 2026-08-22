package com.github.denconcept.restaurantvoting.restaurant.to;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MenuCreateUpdateTo(@NotNull LocalDate menuDate) {
}