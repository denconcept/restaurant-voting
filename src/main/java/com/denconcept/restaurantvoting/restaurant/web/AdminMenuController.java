package com.denconcept.restaurantvoting.restaurant.web;

import com.denconcept.restaurantvoting.common.error.NotFoundException;
import com.denconcept.restaurantvoting.restaurant.model.Menu;
import com.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import com.denconcept.restaurantvoting.restaurant.to.MenuTo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminMenuController {

    public static final String REST_URL = "/api/admin/menus";
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuTo> createWithLocation(@RequestBody MenuTo menuTo) {
        LocalDate menuDate = menuTo.getMenuDate();
        Restaurant restaurant = restaurantRepository.getReferenceById(menuTo.getRestaurantId());
        Menu menu = new Menu(menuDate, restaurant);
        Menu created = menuRepository.save(menu);
        MenuTo createdTo = new MenuTo(created.getId(), created.getMenuDate(), created.getRestaurant().getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(createdTo.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(createdTo);
    }

    @GetMapping("/{id}")
    public MenuTo get(@PathVariable Integer id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu not found with id=" + id));
        return new MenuTo(menu.getId(), menu.getMenuDate(), menu.getRestaurant().getId());
    }

    @GetMapping
    public List<MenuTo> getAll() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuTo> menuTos = new ArrayList<>();
        for (Menu menu : menus) {
            MenuTo menuTo = new MenuTo(menu.getId(), menu.getMenuDate(), menu.getRestaurant().getId());
            menuTos.add(menuTo);
        }
        return menuTos;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody MenuTo menuTo) {
        Integer id = menuTo.getId();
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu not found with id=" + id));
        menu.setMenuDate(menuTo.getMenuDate());
        Restaurant restaurant = restaurantRepository.getReferenceById(menuTo.getRestaurantId());
        menu.setRestaurant(restaurant);
        menuRepository.save(menu);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        menuRepository.deleteById(id);
    }
}