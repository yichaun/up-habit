package com.up.habit.app.controller.validator.type;

import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.validator.HabitValidator;
import com.up.habit.expand.route.anno.Param;

import java.math.BigDecimal;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/7/24 11:37
 */
public class LongValidate extends HabitValidator {
    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        try {
            Long.parseLong(controller.get(param.name()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Class<? extends Comparable> type() {
        return long.class;
    }

    @Override
    public String format() {
        return null;
    }
}
