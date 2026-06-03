package com.denconcept.restaurantvoting.user.repository;

import com.denconcept.restaurantvoting.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
