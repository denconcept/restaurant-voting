package com.denconcept.restaurantvoting.restaurant.to;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RestaurantMenuTo {

    private final Integer restaurantId;
    private final String restaurantName;
    private final List<MenuItemTo> menuItems;
}