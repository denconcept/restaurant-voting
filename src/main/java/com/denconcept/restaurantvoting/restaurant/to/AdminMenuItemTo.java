package com.denconcept.restaurantvoting.restaurant.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class AdminMenuItemTo {

    @NotBlank
    @Size(max = 255)
    private final String name;

    @NotNull
    @Positive
    private final BigDecimal price;

    @NotNull
    private final Integer menuId;
}