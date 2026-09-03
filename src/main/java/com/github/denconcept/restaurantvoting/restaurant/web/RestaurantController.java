package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.restaurant.service.RestaurantService;
import com.github.denconcept.restaurantvoting.restaurant.to.ClientResponseRestaurantMenuItemTo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RestaurantController {

    public static final String REST_URL = "/api/restaurants";
    private final RestaurantService restaurantService;

    @GetMapping()
    public List<ClientResponseRestaurantMenuItemTo> getRestaurantsWithMenuToday() {
        return restaurantService.getRestaurantsWithMenu(LocalDate.now());
    }
}