package com.up.habit.expand.gson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.json.Json;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.up.habit.kit.DateKit;
import com.up.habit.kit.gson.ModelAdapter;
import com.up.habit.kit.gson.RecordAdapter;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/9/18 11:27
 */
public class GsonJson extends Json {
    static Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Record.class, new RecordAdapter());
        builder.registerTypeHierarchyAdapter(Model.class, new ModelAdapter());
        gson = builder.create();

    }

    @Override
    public String toJson(Object object) {
        String dp = datePattern != null ? datePattern : getDefaultDatePattern();
        if (dp == null) {
            return JSON.toJSONString(object);
        } else {
            gson = gson.newBuilder().setDateFormat(DateKit.timeStampPattern).create();
        }
        return gson.toJson(object);
    }

    @Override
    public <T> T parse(String jsonString, Class<T> type) {
        return gson.fromJson(jsonString, type);
    }
}
