package com.denconcept.restaurantvoting.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NamedEntity extends BaseEntity {

    @NotBlank
    @Size(min = 2, max = 64)
    @Column(nullable = false)
    protected String name;

    @Override
    public String toString() {
        return super.toString() + '[' + name + ']';
    }
}
