package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.AbstractControllerTest;
import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.*;
import static com.github.denconcept.restaurantvoting.restaurant.web.AdminRestaurantController.REST_URL;
import static com.github.denconcept.restaurantvoting.user.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;
    private final int restaurantId = MCDONALDS.getId();

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        long before = restaurantRepository.count();
        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRestaurant)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION));
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertEquals(before + 1, restaurants.size());
        Restaurant created = restaurants.stream()
                .filter(r -> r.getName().equals(newRestaurant.getName()))
                .findFirst()
                .orElseThrow();
        assertEquals(newRestaurant.getName(), created.getName());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        mockMvc.perform(put(REST_URL + "/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        assertEquals(updated.getName(), restaurant.getName());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        long before = restaurantRepository.count();
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/{id}", restaurantId))
                .andDo(print())
                .andExpect(status().isNoContent());
        int after = restaurantRepository.findAll().size();
        assertEquals(before - 1, after);
        assertTrue(restaurantRepository.findById(restaurantId).isEmpty());
    }
}