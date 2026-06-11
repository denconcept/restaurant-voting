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
}
