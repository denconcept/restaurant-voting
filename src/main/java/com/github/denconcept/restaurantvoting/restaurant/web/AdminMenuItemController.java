package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.common.error.NotFoundException;
import com.github.denconcept.restaurantvoting.restaurant.model.Menu;
import com.github.denconcept.restaurantvoting.restaurant.model.MenuItem;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuItemRepository;
import com.github.denconcept.restaurantvoting.restaurant.repository.MenuRepository;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuItemAdminTo;
import com.github.denconcept.restaurantvoting.restaurant.to.MenuItemCreateUpdateTo;
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
@RequestMapping(value = AdminMenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminMenuItemController {

    public static final String REST_URL = "/api/admin/menus/{menuId}/items";
    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWithLocation(@PathVariable("menuId") Integer menuId,
                                                   @Valid @RequestBody MenuItemCreateUpdateTo menuItemCreateUpdateTo) {
        log.info("Create menuItem {} for menu {}", menuItemCreateUpdateTo, menuId);
        Menu menu = menuRepository.getExisted(menuId);
        MenuItem menuItem = new MenuItem(menuItemCreateUpdateTo.name(), menuItemCreateUpdateTo.price(), menu);
        MenuItem created = menuItemRepository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).build();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public MenuItemAdminTo get(@PathVariable("menuId") Integer menuId, @PathVariable("id") Integer id) {
        MenuItem menuItem = menuItemRepository.findByIdAndMenuId(id, menuId)
                .orElseThrow(() -> new NotFoundException("MenuItem not found in this menu"));
        return new MenuItemAdminTo(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), menuId);
    }

    @Transactional(readOnly = true)
    @GetMapping
    public List<MenuItemAdminTo> getAll(@PathVariable("menuId") Integer menuId) {
        return menuItemRepository.findAllByMenuIdOrderByIdAsc(menuId).stream()
                .map(mi -> new MenuItemAdminTo(mi.getId(), mi.getName(), mi.getPrice(), menuId))
                .collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("menuId") Integer menuId,
                       @PathVariable("id") Integer id,
                       @Valid @RequestBody MenuItemCreateUpdateTo menuItemCreateUpdateTo) {
        log.info("Update menuItem {} for menu {}", menuItemCreateUpdateTo, menuId);
        MenuItem menuItem = menuItemRepository.findByIdAndMenuId(id, menuId)
                .orElseThrow(() -> new NotFoundException("MenuItem not found in this menu"));
        menuItem.setName(menuItemCreateUpdateTo.name());
        menuItem.setPrice(menuItemCreateUpdateTo.price());
    }

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("menuId") Integer menuId,
                       @PathVariable("id") Integer id) {
        log.info("Delete menuItem {} from menu {}", id, menuId);
        MenuItem menuItem = menuItemRepository.findByIdAndMenuId(id, menuId)
                .orElseThrow(() -> new NotFoundException("MenuItem not found in this menu"));
        menuItemRepository.delete(menuItem);
    }
}