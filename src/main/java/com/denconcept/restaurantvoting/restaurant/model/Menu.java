package com.denconcept.restaurantvoting.restaurant.model;

import com.denconcept.restaurantvoting.common.HasId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_menu_restaurant_date", columnNames = {"restaurant_id", "menu_date"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu implements HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    private List<MenuItem> menuItems;

    public Menu(LocalDate menuDate, Restaurant restaurant) {
        this.menuDate = menuDate;
        this.restaurant = restaurant;
    }
}