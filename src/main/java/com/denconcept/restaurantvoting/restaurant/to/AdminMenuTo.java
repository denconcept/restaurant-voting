package com.denconcept.restaurantvoting.restaurant.to;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class AdminMenuTo {

    @NotNull
    private final LocalDate menuDate;

    @NotNull
    private final Integer restaurantId;
}