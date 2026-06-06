package com.denconcept.restaurantvoting.restaurant.service;

import com.denconcept.restaurantvoting.restaurant.model.Menu;
import com.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.denconcept.restaurantvoting.restaurant.to.MenuItemTo;
import com.denconcept.restaurantvoting.restaurant.to.RestaurantMenuTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public List<RestaurantMenuTo> getRestaurantsWithMenu(LocalDate menuDate) {
        List<RestaurantMenuTo> restaurantMenuTos = new ArrayList<>();
        List<Menu> menus = menuRepository.findAllByMenuDate(menuDate);
        for (Menu menu : menus) {
            List<MenuItemTo> menuItemTos = new ArrayList<>();
            List<MenuItem> menuItems = menu.getMenuItems();
            for (MenuItem menuItem : menuItems) {
                MenuItemTo menuItemTo = new MenuItemTo(menuItem.getName(), menuItem.getPrice());
                menuItemTos.add(menuItemTo);
            }
            Restaurant restaurant = menu.getRestaurant();
            RestaurantMenuTo restaurantMenuTo = new RestaurantMenuTo(
                    restaurant.getId(), restaurant.getName(), menuItemTos);
            restaurantMenuTos.add(restaurantMenuTo);
        }
        return restaurantMenuTos;
    }
}