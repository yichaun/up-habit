package com.up.habit.kit.gson;

import com.google.gson.*;
import com.jfinal.plugin.activerecord.Record;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/9/17 11:40
 */
public class RecordAdapter implements JsonSerializer<Record>, JsonDeserializer<Record> {
    @Override
    public Record deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        Record r = new Record();
        Map<String, Object> m = context.deserialize(jsonElement, HashMap.class);
        r.setColumns(m);
        return r;
    }

    @Override
    public JsonElement serialize(Record record, Type type, JsonSerializationContext context) {
        return context.serialize(record.getColumns());
    }
}
