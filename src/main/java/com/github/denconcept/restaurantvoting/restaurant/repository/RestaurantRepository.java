package com.github.denconcept.restaurantvoting.restaurant.repository;

import com.github.denconcept.restaurantvoting.common.BaseRepository;
import com.github.denconcept.restaurantvoting.restaurant.model.Restaurant;

import java.util.List;

public interface RestaurantRepository extends BaseRepository<Restaurant> {

    List<Restaurant> findAllByOrderByIdAsc();
}