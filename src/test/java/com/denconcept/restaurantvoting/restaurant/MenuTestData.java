package com.denconcept.restaurantvoting.restaurant;

import com.denconcept.restaurantvoting.restaurant.model.Menu;
import com.denconcept.restaurantvoting.restaurant.to.AdminMenuTo;

import java.time.LocalDate;

import static com.denconcept.restaurantvoting.restaurant.RestaurantTestData.BURGER_KING;
import static com.denconcept.restaurantvoting.restaurant.RestaurantTestData.MCDONALDS;
import static com.denconcept.restaurantvoting.restaurant.web.ProfileRestaurantController.TODAY;

public class MenuTestData {

    public static final Menu MCDONALDS_MENU = new Menu(1, TODAY, MCDONALDS);
    public static final Menu BURGER_KING_MENU = new Menu(2, TODAY, BURGER_KING);
    public static final LocalDate TOMORROW = TODAY.plusDays(1);

    public static AdminMenuTo getNewTo() {
        return new AdminMenuTo(TOMORROW, MCDONALDS.getId());
    }

    public static Menu getNew() {
        return new Menu(TOMORROW, MCDONALDS);
    }

    public static AdminMenuTo getUpdated() {
        return new AdminMenuTo(TOMORROW, BURGER_KING.getId());
    }
}