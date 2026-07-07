package com.denconcept.restaurantvoting.restaurant.web;

import com.denconcept.restaurantvoting.restaurant.service.MenuService;
import com.denconcept.restaurantvoting.restaurant.to.RestaurantMenuTo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = ProfileRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProfileRestaurantController {

    public static final String REST_URL = "/api/profile/restaurants";
    private final MenuService menuService;

    @GetMapping
    public List<RestaurantMenuTo> getTodayRestaurants() {
        return menuService.getRestaurantsWithMenu(LocalDate.now());
    }
}