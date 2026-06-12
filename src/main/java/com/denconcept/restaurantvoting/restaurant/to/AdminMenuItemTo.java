package com.denconcept.restaurantvoting.restaurant.to;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class AdminMenuItemTo {

    private final String name;
    private final BigDecimal price;
    private final Integer menuId;
}