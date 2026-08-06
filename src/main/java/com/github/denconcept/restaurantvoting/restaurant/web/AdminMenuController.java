package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.restaurant.model.Menu;
import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.github.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.AdminMenuTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.github.denconcept.restaurantvoting.restaurant.service.MenuService.ADMIN_MENUS_CACHE;
import static com.github.denconcept.restaurantvoting.restaurant.service.MenuService.MENUS_CACHE;

@Slf4j
@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminMenuController {

    public static final String REST_URL = "/api/admin/menus";
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWithLocation(@Valid @RequestBody AdminMenuTo adminMenuTo) {
        log.info("create {}", adminMenuTo);
        LocalDate menuDate = adminMenuTo.menuDate();
        Restaurant restaurant = restaurantRepository.getExisted(adminMenuTo.restaurantId());
        Menu menu = new Menu(menuDate, restaurant);
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).build();
    }

    @GetMapping("/{id}")
    public AdminMenuTo get(@PathVariable Integer id) {
        Menu menu = menuRepository.getExisted(id);
        return new AdminMenuTo(menu.getId(), menu.getMenuDate(), menu.getRestaurant().getId());
    }

    @GetMapping
    public List<AdminMenuTo> getAll() {
        List<Menu> menus = menuRepository.findAll();
        List<AdminMenuTo> adminMenuTos = new ArrayList<>();
        for (Menu menu : menus) {
            AdminMenuTo adminMenuTo = new AdminMenuTo(menu.getId(), menu.getMenuDate(), menu.getRestaurant().getId());
            adminMenuTos.add(adminMenuTo);
        }
        return adminMenuTos;
    }

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody AdminMenuTo adminMenuTo, @PathVariable int id) {
        log.info("update {} with id = {}", adminMenuTo, id);
        Menu menu = menuRepository.getExisted(id);
        menu.setMenuDate(adminMenuTo.menuDate());
        Restaurant restaurant = restaurantRepository.getExisted(adminMenuTo.restaurantId());
        menu.setRestaurant(restaurant);
        menuRepository.save(menu);
    }

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        log.info("delete {}", id);
        Menu menu = menuRepository.getExisted(id);
        menuRepository.delete(menu);
    }
}