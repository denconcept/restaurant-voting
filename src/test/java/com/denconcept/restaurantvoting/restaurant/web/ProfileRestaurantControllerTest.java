package com.denconcept.restaurantvoting.restaurant.web;

import com.denconcept.restaurantvoting.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.denconcept.restaurantvoting.restaurant.MenuItemTestData.BIG_MAC;
import static com.denconcept.restaurantvoting.restaurant.RestaurantTestData.MCDONALDS;
import static com.denconcept.restaurantvoting.restaurant.web.ProfileRestaurantController.REST_URL;
import static com.denconcept.restaurantvoting.user.UserTestData.USER_MAIL;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileRestaurantControllerTest extends AbstractControllerTest {

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
                .andExpect(jsonPath("$[0].menuItems[0].name").value(BIG_MAC.getName()))
                .andExpect(jsonPath("$[0].menuItems[0].price").value(BIG_MAC.getPrice().doubleValue()));
    }

    @Test
    void getTodayRestaurantsUnauthorized() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}