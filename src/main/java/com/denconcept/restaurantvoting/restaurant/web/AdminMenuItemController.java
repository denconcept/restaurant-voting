package com.denconcept.restaurantvoting.restaurant.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AdminMenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminMenuItemController {

    public static final String REST_URL = "/api/admin/menu-items";
}