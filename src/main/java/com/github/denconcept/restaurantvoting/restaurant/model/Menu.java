package com.github.denconcept.restaurantvoting.restaurant.model;

import com.github.denconcept.restaurantvoting.common.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_menu_restaurant_date", columnNames = {"restaurant_id", "menu_date"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @NotNull
    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    private List<MenuItem> menuItems;

    public Menu(LocalDate menuDate, Restaurant restaurant) {
        this(null, menuDate, restaurant);
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
    }
}