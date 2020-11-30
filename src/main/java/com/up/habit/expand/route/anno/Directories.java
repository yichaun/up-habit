package com.up.habit.expand.route.anno;

import java.lang.annotation.*;

/**
 * TODO:目录模块注解
 *
 * @author 王剑洪 on 2020/01/03 09:40
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Directories {
    Directory[] value() default {};

}
