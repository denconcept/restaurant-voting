package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.AbstractControllerTest;
import com.github.denconcept.restaurantvoting.common.error.NotFoundException;
import com.github.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuItemRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuItemCreateUpdateTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.denconcept.restaurantvoting.restaurant.MenuItemTestData.BIG_MAC;
import static com.github.denconcept.restaurantvoting.restaurant.MenuItemTestData.getNewCreateUpdateTo;
import static com.github.denconcept.restaurantvoting.restaurant.MenuTestData.MCDONALDS_MENU;
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
    private final int menuId = MCDONALDS_MENU.getId();
    private final int menuItemId = BIG_MAC.getId();

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItemCreateUpdateTo newMenuItem = getNewCreateUpdateTo();
        int before = menuItemRepository.findAllByMenuIdOrderByIdAsc(menuId).size();
        mockMvc.perform(post(REST_URL, menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newMenuItem)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION));
        List<MenuItem> menuItems = menuItemRepository.findAllByMenuIdOrderByIdAsc(menuId);
        assertEquals(before + 1, menuItems.size());
        MenuItem created = menuItems.stream()
                .filter(mi -> mi.getName().equals(newMenuItem.name()) &&
                        mi.getPrice().compareTo(newMenuItem.price()) == 0)
                .findFirst()
                .orElseThrow();
        assertEquals(newMenuItem.name(), created.getName());
        assertEquals(0, newMenuItem.price().compareTo(created.getPrice()));
        assertEquals(menuId, created.getMenu().getId());
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void update() throws Exception {
        MenuItemCreateUpdateTo updated = getNewCreateUpdateTo();
        mockMvc.perform(put(REST_URL + "/{id}", menuId, menuItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MenuItem menuItem = menuItemRepository.findByIdAndMenuId(menuItemId, menuId)
                .orElseThrow(() -> new NotFoundException("MenuItem not found in this menu"));
        assertEquals(updated.name(), menuItem.getName());
        assertEquals(0, updated.price().compareTo(menuItem.getPrice()));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void delete() throws Exception {
        int before = menuItemRepository.findAllByMenuIdOrderByIdAsc(menuId).size();
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/{id}", menuId, menuItemId))
                .andDo(print())
                .andExpect(status().isNoContent());
        int after = menuItemRepository.findAllByMenuIdOrderByIdAsc(menuId).size();
        assertEquals(before - 1, after);
        assertTrue(menuItemRepository.findByIdAndMenuId(menuItemId, menuId).isEmpty());
    }
}