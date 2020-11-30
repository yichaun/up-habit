package com.up.habit.expand.cache.serializer;

import java.io.Serializable;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/9/17 10:13
 */
public class JsonCacheObject implements Serializable {
    private String clazz;
    private String object;

    public JsonCacheObject() {
    }

    public JsonCacheObject(String clazz, String object) {
        this.clazz = clazz;
        this.object = object;
    }


    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }


}
