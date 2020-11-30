package com.up.habit.app.controller.validator.type;

import com.up.habit.app.controller.HabitController;
import com.up.habit.expand.route.anno.Param;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/24 23:45
 */
public class SortValidate extends IntegerValidate {
    int min = 1;
    int max = 999999;

    @Override
    public Class<? extends Comparable> type() {
        return Integer.class;
    }

    @Override
    public String format() {
        return "1-999999";
    }

    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        try {
            int val = Integer.parseInt(value);
            return val>=min&&val<=max;
        } catch (Exception e) {
            return false;
        }
    }
}
