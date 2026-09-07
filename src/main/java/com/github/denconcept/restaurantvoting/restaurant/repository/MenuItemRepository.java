package com.github.denconcept.restaurantvoting.restaurant.repository;

import com.github.denconcept.restaurantvoting.common.BaseRepository;
import com.github.denconcept.restaurantvoting.restaurant.model.MenuItem;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends BaseRepository<MenuItem> {

    Optional<MenuItem> findByIdAndRestaurantId(Integer id, Integer restaurantId);

    List<MenuItem> findAllByRestaurantIdAndMenuDateOrderByIdAsc(Integer restaurantId, LocalDate menuDate);

    @EntityGraph(attributePaths = "restaurant")
    List<MenuItem> findAllByMenuDate(LocalDate menuDate);
}