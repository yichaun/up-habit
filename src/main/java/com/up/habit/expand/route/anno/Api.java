package com.up.habit.expand.route.anno;

import com.up.habit.expand.route.perm.Auth;

import java.lang.annotation.*;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/26 0:45
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Api {
    /**
     * 方法名称
     *
     * @return
     */
    String value() default "";

    /**
     * 请求方法
     *
     * @return
     */
    String httpMethod() default "post";

    /**
     * 返回内容
     *
     * @return
     */
    String response() default "";

    /**
     * 对应路由路径
     *
     * @return
     */
    String path() default "";

    /**
     * 组件位置
     * @return
     */
    String component() default "";

    /**
     * 方法说明
     *
     * @return
     */
    String des() default "";

    /**
     * 权限
     */
    Auth auth() default Auth.NULL;

    /**
     * auth()=Menu 时候的菜单图标
     */
    String icon() default "";

}
