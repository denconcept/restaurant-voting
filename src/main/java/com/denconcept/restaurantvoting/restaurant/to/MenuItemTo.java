package com.denconcept.restaurantvoting.restaurant.to;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MenuItemTo {

    private String name;
    private BigDecimal price;
}