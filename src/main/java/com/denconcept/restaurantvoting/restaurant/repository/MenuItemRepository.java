package com.denconcept.restaurantvoting.restaurant.repository;

import com.denconcept.restaurantvoting.restaurant.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}
