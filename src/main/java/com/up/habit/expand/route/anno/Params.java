package com.up.habit.expand.route.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO:参数数组
 * <p>
 *
 * @author 王剑洪 on 2019/11/13 13:57
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Params {
    Param[] value() default {};
}
