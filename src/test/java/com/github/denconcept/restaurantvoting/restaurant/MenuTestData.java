package com.github.denconcept.restaurantvoting.restaurant;

import com.github.denconcept.restaurantvoting.restaurant.model.Menu;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuCreateUpdateTo;

import java.time.LocalDate;

import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.MCDONALDS;

public class MenuTestData {

    public static final LocalDate TODAY = LocalDate.now();

    public static final Menu MCDONALDS_MENU = new Menu(1, TODAY, MCDONALDS);
    public static final LocalDate TOMORROW = TODAY.plusDays(1);

    public static MenuCreateUpdateTo getNewCreateUpdateTo() {
        return new MenuCreateUpdateTo(TOMORROW);
    }
}