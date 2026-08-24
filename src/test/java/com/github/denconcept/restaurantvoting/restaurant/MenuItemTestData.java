package com.github.denconcept.restaurantvoting.restaurant;

import com.github.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuItemCreateUpdateTo;

import java.math.BigDecimal;

import static com.github.denconcept.restaurantvoting.restaurant.MenuTestData.MCDONALDS_MENU;

public class MenuItemTestData {

    public static final MenuItem BIG_MAC = new MenuItem(1, "Big Mac", new BigDecimal("6.99"), MCDONALDS_MENU);

    public static MenuItemCreateUpdateTo getNewCreateUpdateTo() {
        return new MenuItemCreateUpdateTo("NewOrUpdate", new BigDecimal("9.99"));
    }
}