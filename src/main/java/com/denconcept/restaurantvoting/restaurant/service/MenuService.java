package com.denconcept.restaurantvoting.restaurant.service;

import com.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.denconcept.restaurantvoting.restaurant.to.MenuItemTo;
import com.denconcept.restaurantvoting.restaurant.to.RestaurantMenuTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    public static final String MENUS_CACHE = "menus";

    private final MenuRepository menuRepository;

    @Cacheable(MENUS_CACHE)
    @Transactional(readOnly = true)
    public List<RestaurantMenuTo> getRestaurantsWithMenu(LocalDate menuDate) {
        log.info("Get restaurants with menu for {}", menuDate);
        return menuRepository.findAllByMenuDate(menuDate).stream()
                .map(menu -> {
                    List<MenuItemTo> menuItems = menu.getMenuItems().stream()
                            .map(menuItem -> new MenuItemTo(menuItem.getName(), menuItem.getPrice()))
                            .toList();
                    Restaurant restaurant = menu.getRestaurant();
                    return new RestaurantMenuTo(restaurant.getId(), restaurant.getName(), menuItems);
                })
                .toList();
    }
}