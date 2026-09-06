package com.github.denconcept.restaurantvoting.restaurant.web;

import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.github.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
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
import java.util.List;

import static com.github.denconcept.restaurantvoting.common.CacheNames.USER_RESTAURANTS_CACHE;
import static com.github.denconcept.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static com.github.denconcept.restaurantvoting.common.validation.ValidationUtil.checkIsNew;

@Slf4j
@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminRestaurantController {

    public static final String REST_URL = "/api/admin/restaurants";
    private final RestaurantRepository restaurantRepository;

    @CacheEvict(value = USER_RESTAURANTS_CACHE, allEntries = true)
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("Create {}", restaurant);
        checkIsNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).build();
    }

    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable("id") Integer id) {
        return restaurantRepository.getExisted(id);
    }

    @Transactional(readOnly = true)
    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantRepository.findAllByOrderByIdAsc();
    }

    @CacheEvict(value = USER_RESTAURANTS_CACHE, allEntries = true)
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") int id,
                       @Valid @RequestBody Restaurant restaurant) {
        log.info("Update {} with id = {}", restaurant, id);
        Restaurant existing = restaurantRepository.getExisted(id);
        assureIdConsistent(restaurant, id);
        existing.setName(restaurant.getName());
    }

    @CacheEvict(value = USER_RESTAURANTS_CACHE, allEntries = true)
    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        log.info("Delete {}", id);
        Restaurant restaurant = restaurantRepository.getExisted(id);
        restaurantRepository.delete(restaurant);
    }
}