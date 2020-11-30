package com.up.habit.kit.gson;

import com.google.gson.*;
import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.Model;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/9/17 11:39
 */
public class ModelListAdapter implements JsonSerializer<Model >, JsonDeserializer<List<Model>> {

    @Override
    public JsonElement serialize(Model m, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(CPI.getAttrs(m));
    }

    @Override
    public List<Model> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Map<String, Object> map = jsonDeserializationContext.deserialize(jsonElement, HashMap.class);
//        Class<Model> modelClass= ClassKit.loadClass(type.getTypeName());
//        Model m=  ClassKit.newInstance(modelClass,true);
//        List<Model> m= newInstance(type);
//        m.put(map);
        return null;
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
