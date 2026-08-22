package com.github.denconcept.restaurantvoting.restaurant.to;

import java.math.BigDecimal;

public record MenuItemAdminTo(Integer id, String name, BigDecimal price, Integer menuId) {
}
