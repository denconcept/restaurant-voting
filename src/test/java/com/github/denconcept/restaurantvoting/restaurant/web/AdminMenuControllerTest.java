package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.AbstractControllerTest;
import com.github.denconcept.restaurantvoting.common.error.NotFoundException;
import com.github.denconcept.restaurantvoting.restaurant.model.Menu;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuCreateUpdateTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.denconcept.restaurantvoting.restaurant.MenuTestData.MCDONALDS_MENU;
import static com.github.denconcept.restaurantvoting.restaurant.MenuTestData.getNewCreateUpdateTo;
import static com.github.denconcept.restaurantvoting.restaurant.RestaurantTestData.MCDONALDS;
import static com.github.denconcept.restaurantvoting.restaurant.web.AdminMenuController.REST_URL;
import static com.github.denconcept.restaurantvoting.user.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuControllerTest extends AbstractControllerTest {

    @Autowired
    private MenuRepository menuRepository;
    private final int restaurantId = MCDONALDS.getId();
    private final int menuId = MCDONALDS_MENU.getId();

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuCreateUpdateTo newMenu = getNewCreateUpdateTo();
        int before = menuRepository.findAllByRestaurantIdOrderByIdAsc(restaurantId).size();
        mockMvc.perform(post(REST_URL, restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMenu)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION));
        List<Menu> menus = menuRepository.findAllByRestaurantIdOrderByIdAsc(restaurantId);
        assertEquals(before + 1, menus.size());
        Menu created = menus.stream()
                .filter(menu -> menu.getMenuDate().equals(newMenu.menuDate()))
                .findFirst()
                .orElseThrow();
        assertEquals(newMenu.menuDate(), created.getMenuDate());
        assertEquals(restaurantId, created.getRestaurant().getId());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        MenuCreateUpdateTo updated = getNewCreateUpdateTo();
        mockMvc.perform(put(REST_URL + "/{id}", restaurantId, menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Menu menu = menuRepository.findByIdAndRestaurantId(menuId, restaurantId)
                .orElseThrow(() -> new NotFoundException("Menu not found in this restaurant"));
        assertEquals(updated.menuDate(), menu.getMenuDate());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        int before = menuRepository.findAllByRestaurantIdOrderByIdAsc(restaurantId).size();
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/{id}", restaurantId, menuId))
                .andDo(print())
                .andExpect(status().isNoContent());
        int after = menuRepository.findAllByRestaurantIdOrderByIdAsc(restaurantId).size();
        assertEquals(before - 1, after);
        assertTrue(menuRepository.findByIdAndRestaurantId(menuId, restaurantId).isEmpty());
    }
}