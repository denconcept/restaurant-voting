package com.github.denconcept.restaurantvoting.restaurant.service;

import com.github.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuItemRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.ClientResponseMenuItemTo;
import com.github.denconcept.restaurantvoting.restaurant.to.ClientResponseRestaurantMenuItemTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.denconcept.restaurantvoting.common.CacheNames.USER_RESTAURANTS_CACHE;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final MenuItemRepository menuItemRepository;

    @Cacheable(value = USER_RESTAURANTS_CACHE, key = "#menuDate")
    @Transactional(readOnly = true)
    public List<ClientResponseRestaurantMenuItemTo> getRestaurantsWithMenu(LocalDate menuDate) {
        log.info("Get restaurants with menu for {}", menuDate);
        return menuItemRepository.findAllByMenuDate(menuDate).stream()
                .collect(Collectors.groupingBy(
                        MenuItem::getRestaurant,
                        LinkedHashMap::new,
                        Collectors.toList()))
                .entrySet().stream()
                .map(entry -> {
                    Restaurant restaurant = entry.getKey();
                    List<MenuItem> menuItems = entry.getValue();
                    return new ClientResponseRestaurantMenuItemTo(
                            restaurant.getId(),
                            restaurant.getName(),
                            menuItems.stream()
                                    .map(mi -> new ClientResponseMenuItemTo(
                                            mi.getName(),
                                            mi.getPrice())).toList());
                }).toList();
    }
}