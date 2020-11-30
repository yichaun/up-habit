package com.up.habit.expand.cache.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.jfinal.json.FastJson;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.LogKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.serializer.ISerializer;
import com.up.habit.kit.ClassKit;
import com.up.habit.kit.GsonKit;
import redis.clients.util.SafeEncoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/9/17 10:11
 */
public class JsonSerializer implements ISerializer {

    private static Log log = Log.getLog(JsonSerializer.class);


    @Override
    public byte[] keyToBytes(String key) {
        return SafeEncoder.encode(key);
    }

    @Override
    public String keyFromBytes(byte[] bytes) {
        return SafeEncoder.encode(bytes);
    }

    @Override
    public byte[] fieldToBytes(Object field) {
        return valueToBytes(field);
    }

    @Override
    public Object fieldFromBytes(byte[] bytes) {
        return valueFromBytes(bytes);
    }

    @Override
    public byte[] valueToBytes(Object value) {
        if (value == null) {
            return null;
        }
        Class clazz = value.getClass();
        JsonCacheObject object = new JsonCacheObject(clazz.getName(), GsonKit.toJson(value));
        return GsonKit.toJson(object).getBytes();
    }

    @Override
    public Object valueFromBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String json = new String(bytes);
        JsonCacheObject cacheObject = GsonKit.parse(json, JsonCacheObject.class);
        Class type = ClassKit.loadClass(cacheObject.getClazz());
        return GsonKit.parse(cacheObject.getObject(),type);
     /*   if (Model.class.isAssignableFrom(type)) {

        } else {
            return cacheObject.getObject();
        }*/

    }
}
