package com.denconcept.restaurantvoting.restaurant.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ProfileRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestaurantController {

    public static final String REST_URL = "/api/profile/restaurants";
}