package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import com.github.denconcept.restaurantvoting.restaurant.service.RestaurantService;
import com.github.denconcept.restaurantvoting.restaurant.to.AdminRestaurantMenuVoteTo;
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
import java.util.List;

import static com.github.denconcept.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static com.github.denconcept.restaurantvoting.common.validation.ValidationUtil.checkIsNew;
import static com.github.denconcept.restaurantvoting.restaurant.service.RestaurantService.ADMIN_MENUS_CACHE;
import static com.github.denconcept.restaurantvoting.restaurant.service.RestaurantService.MENUS_CACHE;

@Slf4j
@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminRestaurantController {

    public static final String REST_URL = "/api/admin/restaurants";
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkIsNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable Integer id) {
        return restaurantRepository.getExisted(id);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/with-menu-and-votes")
    public List<AdminRestaurantMenuVoteTo> getRestaurantsWithMenuAndVotesByDate(@RequestParam LocalDate date) {
        return restaurantService.getRestaurantsWithMenuAndVotes(date);
    }

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id = {}", restaurant, id);
        Restaurant existing = restaurantRepository.getExisted(id);
        assureIdConsistent(restaurant, id);
        existing.setName(restaurant.getName());
        restaurantRepository.save(existing);
    }

    @Caching(evict = {
            @CacheEvict(value = MENUS_CACHE, allEntries = true),
            @CacheEvict(value = ADMIN_MENUS_CACHE, allEntries = true)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        log.info("delete {}", id);
        Restaurant restaurant = restaurantRepository.getExisted(id);
        restaurantRepository.delete(restaurant);
    }
}