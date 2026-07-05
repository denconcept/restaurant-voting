package com.denconcept.restaurantvoting.restaurant.web;

import com.denconcept.restaurantvoting.common.error.NotFoundException;
import com.denconcept.restaurantvoting.restaurant.model.Restaurant;
import com.denconcept.restaurantvoting.restaurant.repository.RestaurantRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.denconcept.restaurantvoting.common.validation.ValidationUtil.assureIdConsistent;
import static com.denconcept.restaurantvoting.common.validation.ValidationUtil.checkIsNew;

@Slf4j
@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminRestaurantController {

    public static final String REST_URL = "/api/admin/restaurants";
    private final RestaurantRepository restaurantRepository;

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
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id = " + id));
    }

    @GetMapping
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id = {}", restaurant, id);
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id = " + id));
        assureIdConsistent(restaurant, id);
        existing.setName(restaurant.getName());
        restaurantRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        log.info("delete {}", id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id = " + id));
        restaurantRepository.delete(restaurant);
    }
}