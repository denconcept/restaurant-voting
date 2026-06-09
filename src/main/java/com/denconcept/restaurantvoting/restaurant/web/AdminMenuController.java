package com.denconcept.restaurantvoting.restaurant.web;

import com.denconcept.restaurantvoting.common.error.NotFoundException;
import com.denconcept.restaurantvoting.restaurant.model.Menu;
import com.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminMenuController {

    public static final String REST_URL = "/api/admin/menus";
    private final MenuRepository menuRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@RequestBody Menu menu) {
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/{id}")
    public Menu get(@PathVariable Integer id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu not found with id=" + id));
    }

    @GetMapping
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Menu menu) {
        menuRepository.save(menu);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        menuRepository.deleteById(id);
    }
}