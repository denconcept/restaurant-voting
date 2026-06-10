package com.denconcept.restaurantvoting.restaurant.to;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MenuTo {

    private final Integer id;
    private final LocalDate menuDate;
    private final Integer restaurantId;
}
