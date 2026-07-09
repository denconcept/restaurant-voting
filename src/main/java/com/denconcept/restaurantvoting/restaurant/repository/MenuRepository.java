package com.denconcept.restaurantvoting.restaurant.repository;

import com.denconcept.restaurantvoting.common.BaseRepository;
import com.denconcept.restaurantvoting.restaurant.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {

    @EntityGraph(attributePaths = {"restaurant", "menuItems"})
    List<Menu> findAllByMenuDate(LocalDate menuDate);
}