package com.denconcept.restaurantvoting.restaurant.to;

import com.denconcept.restaurantvoting.common.HasId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class MenuTo implements HasId {

    private Integer id;
    private LocalDate menuDate;
    private Integer restaurantId;
}
