package com.up.habit.expand.gson;

import com.jfinal.json.FastJsonFactory;
import com.jfinal.json.IJsonFactory;
import com.jfinal.json.Json;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/9/18 11:33
 */
public class GsonJsonFactory implements IJsonFactory {

    private static final GsonJsonFactory me = new GsonJsonFactory();

    public static GsonJsonFactory me() {
        return me;
    }

    @Override
    public Json getJson() {
        return new GsonJson();
    }
}
