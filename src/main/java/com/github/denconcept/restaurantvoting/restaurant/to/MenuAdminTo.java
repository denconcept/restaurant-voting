package com.github.denconcept.restaurantvoting.restaurant.to;

import java.time.LocalDate;

public record MenuAdminTo(Integer id, LocalDate date, Integer restaurantId) {
}