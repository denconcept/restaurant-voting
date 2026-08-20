package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.github.denconcept.restaurantvoting.restaurant.MenuItemTestData.BIG_MAC;
import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.MCDONALDS;
import static com.github.denconcept.restaurantvoting.restaurant.web.RestaurantController.REST_URL;
import static com.github.denconcept.restaurantvoting.user.UserTestData.USER_MAIL;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RestaurantControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(USER_MAIL)
    void getTodayRestaurants() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].restaurantId").value(MCDONALDS.getId()))
                .andExpect(jsonPath("$[0].restaurantName").value(MCDONALDS.getName()))
                .andExpect(jsonPath("$[0].menuItems", hasSize(3)))
                .andExpect(jsonPath("$[0].menuItems[*].name", hasItem(BIG_MAC.getName())))
                .andExpect(jsonPath("$[0].menuItems[*].price", hasItem(BIG_MAC.getPrice().doubleValue())));
    }

    @Test
    void getTodayRestaurantsUnauthorized() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}