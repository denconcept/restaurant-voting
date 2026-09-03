package com.github.denconcept.restaurantvoting.restaurant.to;

import java.util.List;

public record ClientResponseRestaurantMenuItemTo(
        Integer restaurantId,
        String restaurantName,
        List<ClientResponseMenuItemTo> menuItems) {
}