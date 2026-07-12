package com.denconcept.restaurantvoting.restaurant.web;

import com.denconcept.restaurantvoting.AbstractControllerTest;
import com.denconcept.restaurantvoting.common.error.NotFoundException;
import com.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.denconcept.restaurantvoting.restaurant.RestaurantTestData.*;
import static com.denconcept.restaurantvoting.restaurant.web.AdminRestaurantController.REST_URL;
import static com.denconcept.restaurantvoting.user.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRestaurant)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertEquals(5, restaurants.size());
        Restaurant created = restaurants.stream()
                .filter(r -> r.getName().equals(newRestaurant.getName()))
                .findFirst().orElseThrow();
        assertEquals(newRestaurant.getName(), created.getName());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        mockMvc.perform(put(REST_URL + "/" + MCDONALDS.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Restaurant restaurant = restaurantRepository.getExisted(MCDONALDS.getId());
        assertEquals(updated.getName(), restaurant.getName());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        Restaurant restaurant = restaurantRepository.save(getNew());
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + restaurant.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantRepository.getExisted(restaurant.getId()));
    }
}