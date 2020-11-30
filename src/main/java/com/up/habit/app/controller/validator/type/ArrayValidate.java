package com.up.habit.app.controller.validator.type;

import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.render.To;
import com.up.habit.app.controller.validator.HabitValidator;
import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.expand.route.anno.Param;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.StrKit;

import java.io.File;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/6/19 15:57
 */
public class ArrayValidate extends HabitValidator {

    @Override
    public Class<? extends Comparable> type() {
        return String.class;
    }

    @Override
    public String format() {
        return null;
    }

    @Override
    public boolean validate(HabitController controller, Param param) {
        if (param.required()) {
            String[] array = controller.getArray(param.name());
            if (ArrayKit.isNullOrEmpty(array)) {
                ret(controller, param, "不能为空");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        return true;
    }


}
