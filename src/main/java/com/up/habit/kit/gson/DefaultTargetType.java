package com.up.habit.kit.gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/9/21 0:01
 */
public class DefaultTargetType<T> {
    private Type type;
    private Class<T> classType;

    @SuppressWarnings("unchecked")
    public DefaultTargetType() {
        Type superClass = getClass().getGenericSuperclass();
        this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        if (this.type instanceof ParameterizedType) {
            this.classType = (Class<T>) ((ParameterizedType) this.type).getRawType();
        } else {
            this.classType = (Class<T>) this.type;
        }
    }
}
