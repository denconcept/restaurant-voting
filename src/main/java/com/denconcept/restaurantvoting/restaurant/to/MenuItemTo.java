package com.denconcept.restaurantvoting.restaurant.to;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class MenuItemTo {

    private final String name;
    private final BigDecimal price;
}