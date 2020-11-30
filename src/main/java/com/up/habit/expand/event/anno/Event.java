package com.up.habit.expand.event.anno;

import java.lang.annotation.*;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/10/21 17:08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Event {
    boolean async() default true;

    String action();
}
