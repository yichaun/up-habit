package com.up.habit.app.controller.validator.type;

import com.up.habit.app.controller.HabitController;
import com.up.habit.expand.route.anno.Param;
import com.up.habit.kit.StrKit;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/24 23:19
 */
public class MobilValidate extends StringValidate {
    private int LENGTH_MAX_PHONE = 20;

    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        if (value.length() > LENGTH_MAX_PHONE) {
            return false;
        }
        return true;
    }
}
