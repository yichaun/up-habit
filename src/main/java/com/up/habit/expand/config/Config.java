package com.up.habit.expand.config;

import java.lang.annotation.*;

/**
 * TODO:配置注解
 *
 * @author 王剑洪 on 2020/3/26 1:31
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Config {
    /**配置前缀*/
    String prefix();

    /**配置文件*/
    String file() default "";

    /**
     * 列表配置
     */
    String array() default "";
}
