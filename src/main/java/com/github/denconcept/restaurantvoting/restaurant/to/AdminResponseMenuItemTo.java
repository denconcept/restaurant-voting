package com.github.denconcept.restaurantvoting.restaurant.to;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AdminResponseMenuItemTo(
        Integer id,
        String name,
        BigDecimal price,
        Integer restaurantId,
        LocalDate date) {
}