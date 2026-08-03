package com.github.denconcept.restaurantvoting.user.repository;

import com.github.denconcept.restaurantvoting.common.BaseRepository;
import com.github.denconcept.restaurantvoting.user.model.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmailIgnoreCase(String email);
}
