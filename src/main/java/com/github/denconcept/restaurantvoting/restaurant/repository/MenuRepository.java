package com.github.denconcept.restaurantvoting.restaurant.repository;

import com.github.denconcept.restaurantvoting.common.BaseRepository;
import com.github.denconcept.restaurantvoting.restaurant.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {

    @EntityGraph(attributePaths = {"restaurant", "menuItems"})
    List<Menu> findAllByMenuDate(LocalDate menuDate);
}