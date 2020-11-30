package com.up.habit.app.controller.validator.type;

import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.expand.route.anno.Param;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/6/19 15:57
 */
public class IntegerArrayValidate implements IHabitValidate {

    @Override
    public Class<? extends Comparable> type() {
        return String.class;
    }

    @Override
    public String format() {
        return "";
    }

    @Override
    public boolean validate(HabitController controller, Param param) {
        if (param.required()) {
            Integer[] array = controller.getIntArray(param.name());
            return array != null && array.length > 0;
        } else {
            return true;
        }
    }
}
