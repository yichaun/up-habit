package com.up.habit.kit.gson;

import com.google.gson.*;
import com.jfinal.plugin.activerecord.*;
import com.up.habit.expand.db.model.HabitModel;
import com.up.habit.kit.ClassKit;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/9/17 11:39
 */
public class ModelAdapter implements JsonSerializer<Model >, JsonDeserializer<Model> {

    @Override
    public JsonElement serialize(Model m, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(CPI.getAttrs(m));
    }

    @Override
    public Model deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Map<String, Object> map = jsonDeserializationContext.deserialize(jsonElement, HashMap.class);
//        Class<Model> modelClass= ClassKit.loadClass(type.getTypeName());
//        Model m=  ClassKit.newInstance(modelClass,true);
        Model m= newInstance(type);
        m.put(map);
        return m;
    }

    public Model newInstance(Type type){
        Model model=null;
        try {
            model = (Model) ((Class) type).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
