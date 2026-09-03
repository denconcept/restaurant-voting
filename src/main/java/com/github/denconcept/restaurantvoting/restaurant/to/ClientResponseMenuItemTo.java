package com.github.denconcept.restaurantvoting.restaurant.to;

import java.math.BigDecimal;

public record ClientResponseMenuItemTo(
        String name,
        BigDecimal price) {
}