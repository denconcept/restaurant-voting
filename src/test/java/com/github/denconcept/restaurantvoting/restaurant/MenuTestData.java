package com.github.denconcept.restaurantvoting.restaurant;

import com.github.denconcept.restaurantvoting.restaurant.model.Menu;
import com.github.denconcept.restaurantvoting.restaurant.to.AdminMenuTo;

import java.time.LocalDate;

import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.BURGER_KING;
import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.MCDONALDS;
import static com.github.denconcept.restaurantvoting.restaurant.web.ProfileRestaurantController.TODAY;

public class MenuTestData {

    public static final Menu MCDONALDS_MENU = new Menu(1, TODAY, MCDONALDS);
    public static final Menu BURGER_KING_MENU = new Menu(2, TODAY, BURGER_KING);
    public static final LocalDate TOMORROW = TODAY.plusDays(1);

    public static AdminMenuTo getNewTo() {
        return new AdminMenuTo(MCDONALDS_MENU.getId(), TOMORROW, MCDONALDS.getId());
    }

    public static Menu getNew() {
        return new Menu(TOMORROW, MCDONALDS);
    }

    public static AdminMenuTo getUpdated() {
        return new AdminMenuTo(MCDONALDS_MENU.getId(), TOMORROW, BURGER_KING.getId());
    }
}