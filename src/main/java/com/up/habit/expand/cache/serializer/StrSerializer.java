package com.up.habit.expand.cache.serializer;

import com.jfinal.plugin.redis.serializer.ISerializer;
import redis.clients.util.SafeEncoder;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/9/21 10:46
 */
public class StrSerializer implements ISerializer {
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
        return SafeEncoder.encode(value.toString());
    }

    @Override
    public Object valueFromBytes(byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? null : SafeEncoder.encode(bytes);
    }

}
