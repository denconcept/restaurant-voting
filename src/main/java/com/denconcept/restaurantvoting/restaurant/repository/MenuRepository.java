package com.denconcept.restaurantvoting.restaurant.repository;

import com.denconcept.restaurantvoting.restaurant.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @EntityGraph(attributePaths = {"restaurant", "menuItems"})
    List<Menu> findAllByMenuDate(LocalDate menuDate);
}