package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.AbstractControllerTest;
import com.github.denconcept.restaurantvoting.common.error.NotFoundException;
import com.github.denconcept.restaurantvoting.restaurant.model.Menu;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.AdminMenuTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.denconcept.restaurantvoting.restaurant.MenuTestData.*;
import static com.github.denconcept.restaurantvoting.restaurant.web.AdminMenuController.REST_URL;
import static com.github.denconcept.restaurantvoting.user.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuControllerTest extends AbstractControllerTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        AdminMenuTo newMenu = getNewTo();
        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMenu)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        List<Menu> menus = menuRepository.findAll();
        assertEquals(5, menus.size());
        Menu created = menus.stream()
                .filter(m -> m.getMenuDate().equals(newMenu.menuDate()) &&
                        m.getRestaurant().getId().equals(newMenu.restaurantId()))
                .findFirst()
                .orElseThrow();
        assertEquals(newMenu.menuDate(), created.getMenuDate());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        AdminMenuTo updated = getUpdated();
        mockMvc.perform(put(REST_URL + "/" + MCDONALDS_MENU.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Menu menu = menuRepository.getExisted(MCDONALDS_MENU.getId());
        assertEquals(updated.menuDate(), menu.getMenuDate());
        assertEquals(updated.restaurantId(), menu.getRestaurant().getId());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        Menu menu = menuRepository.save(getNew());
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + menu.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> menuRepository.getExisted(menu.getId()));
    }
}