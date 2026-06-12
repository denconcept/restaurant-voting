package com.denconcept.restaurantvoting.restaurant.web;

import com.denconcept.restaurantvoting.common.error.NotFoundException;
import com.denconcept.restaurantvoting.restaurant.model.Menu;
import com.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.denconcept.restaurantvoting.restaurant.repository.MenuItemRepository;
import com.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.denconcept.restaurantvoting.restaurant.to.AdminMenuItemTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = AdminMenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminMenuItemController {

    public static final String REST_URL = "/api/admin/menu-items";
    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWithLocation(@Valid @RequestBody AdminMenuItemTo adminMenuItemTo) {
        Menu menu = menuRepository.getReferenceById(adminMenuItemTo.getMenuId());
        MenuItem menuItem = new MenuItem(adminMenuItemTo.getName(), adminMenuItemTo.getPrice(), menu);
        MenuItem created = menuItemRepository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).build();
    }

    @GetMapping("/{id}")
    public AdminMenuItemTo get(@PathVariable Integer id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("MenuItem not found with id=" + id));
        return new AdminMenuItemTo(menuItem.getName(), menuItem.getPrice(), menuItem.getMenu().getId());
    }

    @GetMapping
    public List<AdminMenuItemTo> getAll() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        List<AdminMenuItemTo> menuItemTos = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            AdminMenuItemTo adminMenuItemTo =
                    new AdminMenuItemTo(menuItem.getName(), menuItem.getPrice(), menuItem.getMenu().getId());
            menuItemTos.add(adminMenuItemTo);
        }
        return menuItemTos;
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody AdminMenuItemTo adminMenuItemTo, @PathVariable int id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("MenuItem not found with id = " + id));
        menuItem.setName(adminMenuItemTo.getName());
        menuItem.setPrice(adminMenuItemTo.getPrice());
        menuItem.setMenu(menuRepository.getReferenceById(adminMenuItemTo.getMenuId()));
        menuItemRepository.save(menuItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        menuItemRepository.deleteById(id);
    }
}