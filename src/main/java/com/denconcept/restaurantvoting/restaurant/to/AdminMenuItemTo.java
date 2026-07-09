package com.denconcept.restaurantvoting.restaurant.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AdminMenuItemTo(
        @NotBlank @Size(max = 255) String name, @NotNull @Positive BigDecimal price, @NotNull Integer menuId) {
}