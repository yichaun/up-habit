package com.up.habit.expand.route.anno;

import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.app.controller.validator.type.StringValidate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO:
 *
 * @author 王剑洪 on 2019/10/13 1:08
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    /**
     * 参数名
     *
     * @return
     */
    String name() default "";

    /**
     * 参数类型,并且验证
     *
     * @return
     */
    Class<? extends IHabitValidate> dataType() default StringValidate.class;

    /**
     * 参数说明
     *
     * @return
     */
    String des() default "";

    /**
     * 参数默认值
     *
     * @return
     */
    String defaultValue() default "";

    /**
     * 是否必传参数
     *
     * @return
     */
    boolean required() default true;

    String dict() default "";

    int length() default -1;
}
