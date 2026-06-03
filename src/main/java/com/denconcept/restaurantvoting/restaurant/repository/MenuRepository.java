package com.denconcept.restaurantvoting.restaurant.repository;

import com.denconcept.restaurantvoting.restaurant.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
