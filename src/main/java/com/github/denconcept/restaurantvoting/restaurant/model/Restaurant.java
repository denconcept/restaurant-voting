package com.github.denconcept.restaurantvoting.restaurant.model;

import com.github.denconcept.restaurantvoting.common.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_restaurant_name", columnNames = {"name"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant extends NamedEntity {

    public Restaurant(String name) {
        this(null, name);
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }
}