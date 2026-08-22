package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.common.error.NotFoundException;
import com.github.denconcept.restaurantvoting.restaurant.model.Menu;
import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.github.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuAdminTo;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuCreateUpdateTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.denconcept.restaurantvoting.restaurant.service.RestaurantService.ADMIN_MENUS_CACHE;
import static com.github.denconcept.restaurantvoting.restaurant.service.RestaurantService.MENUS_CACHE;

@Slf4j
@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminMenuController {

    public static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWithLocation(@PathVariable("restaurantId") Integer restaurantId,
                                                   @Valid @RequestBody MenuCreateUpdateTo menuCreateUpdateTo) {
        log.info("Create menu {} for restaurant {}", menuCreateUpdateTo, restaurantId);
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        Menu menu = new Menu(menuCreateUpdateTo.menuDate(), restaurant);
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).build();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public MenuAdminTo get(@PathVariable("restaurantId") Integer restaurantId, @PathVariable("id") Integer id) {
        Menu menu = menuRepository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new NotFoundException("Menu not found in this restaurant"));
        return new MenuAdminTo(menu.getId(), menu.getMenuDate(), restaurantId);
    }

    @Transactional(readOnly = true)
    @GetMapping
    public List<MenuAdminTo> getAll(@PathVariable("restaurantId") Integer restaurantId) {
        return menuRepository.findAllByRestaurantIdOrderByIdAsc(restaurantId).stream()
                .map(m -> new MenuAdminTo(m.getId(), m.getMenuDate(), restaurantId))
                .collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("restaurantId") Integer restaurantId,
                       @PathVariable("id") Integer id,
                       @Valid @RequestBody MenuCreateUpdateTo menuCreateUpdateTo) {
        log.info("Update menu {} for restaurant {}", menuCreateUpdateTo, restaurantId);
        Menu menu = menuRepository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new NotFoundException("Menu not found in this restaurant"));
        menu.setMenuDate(menuCreateUpdateTo.menuDate());
    }

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("restaurantId") Integer restaurantId,
                       @PathVariable("id") Integer id) {
        log.info("Delete menu {} from restaurant {}", id, restaurantId);
        Menu menu = menuRepository.findByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new NotFoundException("Menu not found in this restaurant"));
        menuRepository.delete(menu);
    }
}