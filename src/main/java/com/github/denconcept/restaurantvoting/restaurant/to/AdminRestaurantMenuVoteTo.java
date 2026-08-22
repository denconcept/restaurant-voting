package com.github.denconcept.restaurantvoting.restaurant.to;

import java.util.List;

public record AdminRestaurantMenuVoteTo(Integer restaurantId, String restaurantName, Long voteCount, Integer menuId,
                                        List<MenuItemClientTo> menuItems) {
}