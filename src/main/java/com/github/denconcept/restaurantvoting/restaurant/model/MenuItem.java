package com.github.denconcept.restaurantvoting.restaurant.model;

import com.github.denconcept.restaurantvoting.common.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "menu_item",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_menu_item_restaurant_date_name",
                        columnNames = {"restaurant_id", "menu_date", "name"}
                )
        })
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
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    @NotNull
    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    public MenuItem(String name, BigDecimal price, Restaurant restaurant, LocalDate menuDate) {
        this(null, name, price, restaurant, menuDate);
    }

    public MenuItem(Integer id, String name, BigDecimal price, Restaurant restaurant, LocalDate menuDate) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
        this.menuDate = menuDate;
    }
}