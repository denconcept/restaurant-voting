package com.denconcept.restaurantvoting.user.repository;

import com.denconcept.restaurantvoting.common.BaseRepository;
import com.denconcept.restaurantvoting.user.model.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmailIgnoreCase(String email);
}
