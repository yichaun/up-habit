package com.up.habit.kit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.up.habit.expand.gen.model.Table;
import com.up.habit.kit.gson.ModelAdapter;
import com.up.habit.kit.gson.RecordAdapter;

import java.io.Reader;
import java.lang.reflect.Type;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/22 15:58
 */
public class GsonKit {
    private static final Gson GSON = build();


    public static final Gson build() {
        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Record.class, new RecordAdapter());
        builder.registerTypeHierarchyAdapter(Model.class, new ModelAdapter());
        builder.setDateFormat(DateKit.timeStampPattern);
        builder.serializeNulls();
        return builder.create();
    }

    public static String toJson(Object target) {
        return GSON.toJson(target);
    }

    public static final <V> V parse(String json, Class<V> type) {
        return GSON.fromJson(json, type);
    }

    public static final <V> V parse(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static final <V> V parse(Reader reader, Class<V> type) {
        return GSON.fromJson(reader, type);
    }

    public static final <V> V parse(Reader reader, Type type) {
        return GSON.fromJson(reader, type);
    }


}
