package com.denconcept.restaurantvoting.restaurant.web;

import com.denconcept.restaurantvoting.AbstractControllerTest;
import com.denconcept.restaurantvoting.common.error.NotFoundException;
import com.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.denconcept.restaurantvoting.restaurant.repository.MenuItemRepository;
import com.denconcept.restaurantvoting.restaurant.to.AdminMenuItemTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.denconcept.restaurantvoting.restaurant.MenuItemTestData.*;
import static com.denconcept.restaurantvoting.restaurant.web.AdminMenuItemController.REST_URL;
import static com.denconcept.restaurantvoting.user.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuItemControllerTest extends AbstractControllerTest {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        AdminMenuItemTo newMenuItem = getNewTo();
        mockMvc.perform(post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMenuItem)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
        List<MenuItem> menuItems = menuItemRepository.findAll();
        assertEquals(13, menuItems.size());
        MenuItem created = menuItems.stream()
                .filter(i -> i.getName().equals(newMenuItem.name()) &&
                        i.getPrice().compareTo(newMenuItem.price()) == 0 &&
                        i.getMenu().getId().equals(newMenuItem.menuId()))
                .findFirst()
                .orElseThrow();
        assertEquals(newMenuItem.name(), created.getName());
        assertEquals(0, newMenuItem.price().compareTo(created.getPrice()));
        assertEquals(newMenuItem.menuId(), created.getMenu().getId());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        AdminMenuItemTo updated = getUpdated();
        mockMvc.perform(put(REST_URL + "/" + BIG_MAC.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MenuItem menuItem = menuItemRepository.getExisted(BIG_MAC.getId());
        assertEquals(updated.name(), menuItem.getName());
        assertEquals(0, updated.price().compareTo(menuItem.getPrice()));
        assertEquals(updated.menuId(), menuItem.getMenu().getId());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        MenuItem menuItem = menuItemRepository.save(getNew());
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + menuItem.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> menuItemRepository.getExisted(menuItem.getId()));
    }
}