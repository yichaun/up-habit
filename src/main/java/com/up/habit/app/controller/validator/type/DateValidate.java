package com.up.habit.app.controller.validator.type;

import com.jfinal.core.Controller;
import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.validator.HabitValidator;
import com.up.habit.expand.route.anno.Param;
import com.up.habit.kit.DateKit;
import com.up.habit.kit.ValidatorKit;

import java.util.Date;

/**
 * TODO:日期格式验证
 *
 * @author 王剑洪 on 2020/5/24 23:15
 */
public class DateValidate extends HabitValidator {

    @Override
    public Class<? extends Comparable> type() {
        return Date.class;
    }

    @Override
    public String format() {
        return DateKit.datePattern;
    }

    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        return ValidatorKit.isDate(value);
    }
}
