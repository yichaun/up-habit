package com.up.habit.app.controller.validator.type;

import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.validator.HabitValidator;
import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.expand.route.anno.Param;

/**
 * TODO:整数格式验证
 *
 * @author 王剑洪 on 2020/5/24 23:00
 */
public class IntegerValidate extends HabitValidator {


    @Override
    public Class<? extends Comparable> type() {
        return Integer.class;
    }

    @Override
    public String format() {
        return "";
    }

    @Override
    public boolean validateFormat(String value, HabitController controller, Param param) {
        try{
            Integer val=Integer.parseInt(value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
