package com.up.habit.app.controller.validator.type;

import com.up.habit.app.controller.HabitController;
import com.up.habit.expand.route.anno.Param;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/24 23:49
 */
public class SwitchValidate extends IntegerValidate {
    int yes = 1;
    int no = 0;

    @Override
    public Class<? extends Comparable> type() {
        return String.class;
    }

    @Override
    public String format() {
        return "0:否,1:是,或者false,true";
    }

    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        try {
            if (value.length() > 1) {
                Boolean.valueOf(value);
                return true;
            } else {
                int val = Integer.parseInt(value);
                return val == 1 || val == 0;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
