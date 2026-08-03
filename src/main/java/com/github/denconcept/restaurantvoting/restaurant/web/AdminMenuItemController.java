package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.restaurant.model.Menu;
import com.github.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuItemRepository;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.AdminMenuItemTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.github.denconcept.restaurantvoting.restaurant.service.MenuService.MENUS_CACHE;

@Slf4j
@RestController
@RequestMapping(value = AdminMenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminMenuItemController {

    public static final String REST_URL = "/api/admin/menu-items";
    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;

    @CacheEvict(value = MENUS_CACHE, allEntries = true)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWithLocation(@Valid @RequestBody AdminMenuItemTo adminMenuItemTo) {
        log.info("create {}", adminMenuItemTo);
        Menu menu = menuRepository.getExisted(adminMenuItemTo.menuId());
        MenuItem menuItem = new MenuItem(adminMenuItemTo.name(), adminMenuItemTo.price(), menu);
        MenuItem created = menuItemRepository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).build();
    }

    @GetMapping("/{id}")
    public AdminMenuItemTo get(@PathVariable Integer id) {
        MenuItem menuItem = menuItemRepository.getExisted(id);
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

    @CacheEvict(value = MENUS_CACHE, allEntries = true)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody AdminMenuItemTo adminMenuItemTo, @PathVariable int id) {
        log.info("update {} with id = {}", adminMenuItemTo, id);
        MenuItem menuItem = menuItemRepository.getExisted(id);
        menuItem.setName(adminMenuItemTo.name());
        menuItem.setPrice(adminMenuItemTo.price());
        Menu menu = menuRepository.getExisted(adminMenuItemTo.menuId());
        menuItem.setMenu(menu);
        menuItemRepository.save(menuItem);
    }

    @CacheEvict(value = MENUS_CACHE, allEntries = true)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        log.info("delete {}", id);
        MenuItem menuItem = menuItemRepository.getExisted(id);
        menuItemRepository.delete(menuItem);
    }
}