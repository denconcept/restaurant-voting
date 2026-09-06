package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.common.error.NotFoundException;
import com.github.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuItemRepository;
import com.github.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.AdminResponseMenuItemTo;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuItemRequestTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.denconcept.restaurantvoting.common.CacheNames.USER_RESTAURANTS_CACHE;

@Slf4j
@RestController
@RequestMapping(value = AdminMenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminMenuItemController {

    public static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menu-items";
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    @CacheEvict(value = USER_RESTAURANTS_CACHE, allEntries = true)
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWithLocation(@PathVariable("restaurantId") Integer restaurantId,
                                                   @Valid @RequestBody MenuItemRequestTo menuItemRequestTo) {
        log.info("Create {} for restaurant {}", menuItemRequestTo, restaurantId);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        MenuItem menuItem = new MenuItem(
                menuItemRequestTo.name(),
                menuItemRequestTo.price(),
                restaurant,
                menuItemRequestTo.date());
        MenuItem created = menuItemRepository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).build();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public AdminResponseMenuItemTo get(@PathVariable("restaurantId") Integer restaurantId,
                                       @PathVariable("id") Integer id) {
        MenuItem menuItem = menuItemRepository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new NotFoundException("MenuItem not found in this restaurant"));
        return toTo(menuItem);
    }

    @Transactional(readOnly = true)
    @GetMapping
    public List<AdminResponseMenuItemTo> getAll(@PathVariable("restaurantId") Integer restaurantId,
                                                @RequestParam LocalDate date) {
        restaurantRepository.getExisted(restaurantId);
        return menuItemRepository.findAllByRestaurantIdAndMenuDateOrderByIdAsc(restaurantId, date)
                .stream()
                .map(AdminMenuItemController::toTo)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = USER_RESTAURANTS_CACHE, allEntries = true)
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("restaurantId") Integer restaurantId,
                       @PathVariable("id") Integer id,
                       @Valid @RequestBody MenuItemRequestTo menuItemRequestTo) {
        log.info("Update {} for restaurant {}", menuItemRequestTo, restaurantId);
        MenuItem menuItem = menuItemRepository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new NotFoundException("MenuItem not found in this restaurant"));
        menuItem.setName(menuItemRequestTo.name());
        menuItem.setPrice(menuItemRequestTo.price());
        menuItem.setMenuDate(menuItemRequestTo.date());
    }

    @CacheEvict(value = USER_RESTAURANTS_CACHE, allEntries = true)
    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("restaurantId") Integer restaurantId,
                       @PathVariable("id") Integer id) {
        log.info("Delete menuItem {} from restaurant {}.", id, restaurantId);
        MenuItem menuItem = menuItemRepository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new NotFoundException("MenuItem not found in this restaurant"));
        menuItemRepository.delete(menuItem);
    }

    private static AdminResponseMenuItemTo toTo(MenuItem menuItem) {
        return new AdminResponseMenuItemTo(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getPrice(),
                menuItem.getRestaurant().getId(),
                menuItem.getMenuDate()
        );
    }
}