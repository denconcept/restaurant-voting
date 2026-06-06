package com.denconcept.restaurantvoting.restaurant.to;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RestaurantMenuTo {

    private Integer restaurantId;
    private String restaurantName;
    private List<MenuItemTo> menuItems;
}