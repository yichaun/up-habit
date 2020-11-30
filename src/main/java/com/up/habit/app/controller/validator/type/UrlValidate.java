package com.up.habit.app.controller.validator.type;

import com.up.habit.app.controller.HabitController;
import com.up.habit.expand.route.anno.Param;
import com.up.habit.kit.ValidatorKit;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/24 23:20
 */
public class UrlValidate extends StringValidate {

    @Override
    public String format() {
        return "http://xxxxxxxx.xxx https://xxxxxxxx.xxx ";
    }


    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        return ValidatorKit.isUrl(value);
    }


}
