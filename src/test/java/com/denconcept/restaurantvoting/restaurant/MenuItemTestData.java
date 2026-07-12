package com.denconcept.restaurantvoting.restaurant;

import com.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.denconcept.restaurantvoting.restaurant.to.AdminMenuItemTo;

import java.math.BigDecimal;

import static com.denconcept.restaurantvoting.restaurant.MenuTestData.BURGER_KING_MENU;
import static com.denconcept.restaurantvoting.restaurant.MenuTestData.MCDONALDS_MENU;

public class MenuItemTestData {

    public static final MenuItem BIG_MAC = new MenuItem(1, "Big Mac", new BigDecimal("6.99"), MCDONALDS_MENU);

    public static AdminMenuItemTo getNewTo() {
        return new AdminMenuItemTo("New", new BigDecimal("9.99"), MCDONALDS_MENU.getId());
    }

    public static MenuItem getNew() {
        return new MenuItem("New", new BigDecimal("9.99"), MCDONALDS_MENU);
    }

    public static AdminMenuItemTo getUpdated() {
        return new AdminMenuItemTo("Updated", new BigDecimal("5"), BURGER_KING_MENU.getId());
    }
}