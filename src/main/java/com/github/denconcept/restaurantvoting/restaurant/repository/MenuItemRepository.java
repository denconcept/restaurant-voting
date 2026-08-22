package com.github.denconcept.restaurantvoting.restaurant.repository;

import com.github.denconcept.restaurantvoting.common.BaseRepository;
import com.github.denconcept.restaurantvoting.restaurant.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends BaseRepository<MenuItem> {

    Optional<MenuItem> findByIdAndMenuId(Integer id, Integer menuId);

    List<MenuItem> findAllByMenuIdOrderByIdAsc(Integer menuId);
}