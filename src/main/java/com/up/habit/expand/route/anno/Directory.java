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
public @interface Directory {
    /**
     * 目录名
     *
     * @return
     */
    String value() default "";

    /**
     * 图标
     *
     * @return
     */
    String icon() default "";

    /**
     * 组件
     *
     * @return
     */
    String component() default "Layout";

    /**
     *
     * @return
     */
    String path() default "";

}
