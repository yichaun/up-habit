package com.up.habit.app.controller.validator.type;

import com.jfinal.core.Controller;
import com.up.habit.Habit;
import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.validator.HabitValidator;
import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.expand.route.anno.Param;

import java.io.File;

/**
 * TODO:文件格式验证
 *
 * @author 王剑洪 on 2020/5/24 23:50
 */
public class FileValidate extends HabitValidator {
    @Override
    public Class<? extends Comparable> type() {
        return File.class;
    }

    @Override
    public String format() {
        return "文件";
    }

    @Override
    public boolean validate(HabitController controller, Param param) {
        return true;
    }

    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        return true;
    }


}
