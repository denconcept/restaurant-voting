package com.github.denconcept.restaurantvoting.user;

import com.github.denconcept.restaurantvoting.user.model.Role;
import com.github.denconcept.restaurantvoting.user.model.User;

import java.util.Set;

public class UserTestData {

    public static final String USER_MAIL = "user@gmail.com";
    public static final String ADMIN_MAIL = "admin@gmail.com";

    public static final User USER = new User(1, USER_MAIL, "user", Set.of(Role.USER));
}
