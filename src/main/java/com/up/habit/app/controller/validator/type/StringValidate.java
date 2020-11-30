package com.up.habit.app.controller.validator.type;

import com.jfinal.core.Controller;
import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.validator.HabitValidator;
import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.expand.route.anno.Param;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/24 22:59
 */
public class StringValidate extends HabitValidator {

    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        if (param.length() > 0 && value.length() > param.length()) {
            ret(controller, param, "长度不能超过" + param.length() + "!");
            return false;
        }
        return true;
    }

    @Override
    public Class<? extends Comparable> type() {
        return String.class;
    }

    @Override
    public String format() {
        return "";
    }
}
