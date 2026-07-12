package com.denconcept.restaurantvoting.restaurant;

import com.denconcept.restaurantvoting.restaurant.model.Restaurant;

public class RestaurantTestData {

    public static final Restaurant MCDONALDS = new Restaurant(1, "McDonalds");
    public static final Restaurant BURGER_KING = new Restaurant(2, "Burger King");
    public static final int NOT_FOUND_RESTAURANT_ID = 999;

    public static Restaurant getNew() {
        return new Restaurant("New");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(MCDONALDS.getId(), "Updated");
    }
}