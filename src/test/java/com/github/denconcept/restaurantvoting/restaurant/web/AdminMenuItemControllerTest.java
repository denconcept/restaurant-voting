package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.AbstractControllerTest;
import com.github.denconcept.restaurantvoting.common.error.NotFoundException;
import com.github.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuItemRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuItemRequestTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.denconcept.restaurantvoting.restaurant.MenuItemTestData.*;
import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.MCDONALDS;
import static com.github.denconcept.restaurantvoting.restaurant.web.AdminMenuItemController.REST_URL;
import static com.github.denconcept.restaurantvoting.user.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuItemControllerTest extends AbstractControllerTest {

    @Autowired
    private MenuItemRepository menuItemRepository;
    private final int restaurantId = MCDONALDS.getId();
    private final int menuItemId = BIG_MAC.getId();

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItemRequestTo newTo = getNewTo();
        int before = menuItemRepository.findAllByRestaurantIdAndMenuDateOrderByIdAsc(restaurantId, TODAY).size();
        mockMvc.perform(post(REST_URL, restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION));
        List<MenuItem> menuItems = menuItemRepository.findAllByRestaurantIdAndMenuDateOrderByIdAsc(restaurantId, TODAY);
        assertEquals(before + 1, menuItems.size());
        MenuItem created = menuItems.stream()
                .filter(mi -> mi.getName().equals(newTo.name()) &&
                        mi.getPrice().compareTo(newTo.price()) == 0 &&
                        mi.getMenuDate().isEqual(newTo.date()))
                .findFirst()
                .orElseThrow();
        assertEquals(newTo.name(), created.getName());
        assertEquals(0, newTo.price().compareTo(created.getPrice()));
        assertEquals(restaurantId, created.getRestaurant().getId());
        assertEquals(newTo.date(), created.getMenuDate());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        MenuItemRequestTo updated = getUpdatedTo();
        mockMvc.perform(put(REST_URL + "/{id}", restaurantId, menuItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MenuItem menuItem = menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId)
                .orElseThrow(() -> new NotFoundException("MenuItem not found in this restaurant"));
        assertEquals(updated.name(), menuItem.getName());
        assertEquals(0, updated.price().compareTo(menuItem.getPrice()));
        assertEquals(updated.date(), menuItem.getMenuDate());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        int before = menuItemRepository.findAllByRestaurantIdAndMenuDateOrderByIdAsc(restaurantId, TODAY).size();
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/{id}", restaurantId, menuItemId))
                .andDo(print())
                .andExpect(status().isNoContent());
        int after = menuItemRepository.findAllByRestaurantIdAndMenuDateOrderByIdAsc(restaurantId, TODAY).size();
        assertEquals(before - 1, after);
        assertTrue(menuItemRepository.findByIdAndRestaurantId(menuItemId, restaurantId).isEmpty());
    }
}