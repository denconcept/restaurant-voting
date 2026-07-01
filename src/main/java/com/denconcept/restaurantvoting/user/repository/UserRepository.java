package com.denconcept.restaurantvoting.user.repository;

import com.denconcept.restaurantvoting.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmailIgnoreCase(String email);
}
