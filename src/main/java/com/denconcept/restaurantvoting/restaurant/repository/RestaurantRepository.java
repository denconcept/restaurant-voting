package com.denconcept.restaurantvoting.restaurant.repository;

import com.denconcept.restaurantvoting.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
