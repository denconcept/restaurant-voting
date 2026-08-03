package com.github.denconcept.restaurantvoting.restaurant.model;

import com.github.denconcept.restaurantvoting.common.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_item",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_menu_item_menu_name", columnNames = {"menu_id", "name"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuItem extends NamedEntity {

    @NotNull
    @Positive
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    public MenuItem(String name, BigDecimal price, Menu menu) {
        this(null, name, price, menu);
    }

    public MenuItem(Integer id, String name, BigDecimal price, Menu menu) {
        super(id, name);
        this.price = price;
        this.menu = menu;
    }
}