package com.denconcept.restaurantvoting.restaurant.to;

import com.denconcept.restaurantvoting.common.HasId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AdminMenuItemTo implements HasId {

    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer menuId;
}
