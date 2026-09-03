package com.github.denconcept.restaurantvoting.restaurant;

import com.github.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuItemRequestTo;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.MCDONALDS;

public class MenuItemTestData {

    public static final LocalDate TODAY = LocalDate.now();
    public static final LocalDate TOMORROW = TODAY.plusDays(1);
    public static final MenuItem BIG_MAC = new MenuItem(1, "Big Mac", new BigDecimal("6.99"), MCDONALDS, TODAY);

    public static MenuItemRequestTo getNewTo() {
        return new MenuItemRequestTo("New", new BigDecimal("9.99"), TODAY);
    }

    public static MenuItemRequestTo getUpdatedTo() {
        return new MenuItemRequestTo("Updated", new BigDecimal("10.99"), TOMORROW);
    }
}