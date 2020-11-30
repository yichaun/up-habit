package com.up.habit.app.controller.validator.type;

import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.validator.HabitValidator;
import com.up.habit.expand.route.anno.Param;
import com.up.habit.kit.ValidatorKit;

/**
 * TODO:邮箱格式验证
 *
 * @author 王剑洪 on 2020/5/24 23:15
 */
public class EmailValidate extends StringValidate {

    private int LENGTH_MAX_EMAIL = 50;

    @Override
    public String format() {
        return "xxxx@xxx.xx";
    }

    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        if (value.length() > LENGTH_MAX_EMAIL) {
            ret(controller, param, "长度不能超过" + param.length() + "!");
            return false;
        }
        return ValidatorKit.isEmail(value);
    }
}
