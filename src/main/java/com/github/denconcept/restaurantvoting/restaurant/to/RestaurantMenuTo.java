package com.github.denconcept.restaurantvoting.restaurant.to;

import java.util.List;

public record RestaurantMenuTo(Integer restaurantId, String restaurantName, List<MenuItemTo> menuItems) {
}