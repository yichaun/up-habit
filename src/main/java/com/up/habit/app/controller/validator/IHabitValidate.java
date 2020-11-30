package com.up.habit.app.controller.validator;

import com.up.habit.app.controller.HabitController;
import com.up.habit.expand.route.anno.Param;

/**
 * TODO:验证类型
 *
 * @author 王剑洪 on 2020/5/8 10:09
 */
public interface IHabitValidate {

    /**
     * TODO:参数类型
     *
     * @param
     * @return java.lang.String
     * @Author 王剑洪 on 2020/5/24 23:12
     **/
    Class<? extends Comparable> type();

    /**
     * TODO:格式
     *
     * @param
     * @return java.lang.String
     * @Author 王剑洪 on 2020/5/24 18:18
     **/
    String format();

    /**
     * TODO:验证参数
     *
     * @param controller
     * @param param
     * @return boolean
     * @Author 王剑洪 on 2020/5/24 18:19
     **/
    boolean validate(HabitController controller, Param param) ;


}
