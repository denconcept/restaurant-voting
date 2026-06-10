package com.denconcept.restaurantvoting.restaurant.to;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMenuItemTo {

    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer menuId;
}
