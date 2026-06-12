package com.denconcept.restaurantvoting.common.validation;

import com.denconcept.restaurantvoting.common.HasId;
import com.denconcept.restaurantvoting.common.error.IllegalRequestDataException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtil {

    public static void checkIsNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id = null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id = " + id);
        }
    }
}
