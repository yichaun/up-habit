package com.up.habit.expand.cache.redis;

import com.google.gson.Gson;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.up.habit.Habit;
import com.up.habit.expand.cache.IHabitCache;
import com.up.habit.kit.StrKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/27 1:46
 */
public class HabitRedisCache implements IHabitCache {

    private static RedisConfig redisConfig = Habit.config(RedisConfig.class);
    private static String CACHE_NAME = redisConfig.getCacheName();
    private static final String CACHE_NAMES_KEY = "_all:cache:names";

    public Cache getRedis() {
        if (StrKit.notBlank(CACHE_NAME)) {
            return Redis.use(CACHE_NAME);
        } else {
            return Redis.use();
        }
    }

    private String buildKey(String cacheName, Object key) {
        cacheName = buildCacheName(cacheName);
        return String.format("%s:%s", cacheName, key);
    }

    private String buildCacheName(String cacheName) {
        return CACHE_NAME + (StrKit.isBlank(CACHE_NAME) ? "" : ":") + cacheName;
    }


    @Override
    public <T> T get(String cacheName, Object key) {
        return getRedis().get(buildKey(cacheName, key));
    }

    @Override
    public void put(String cacheName, Object key, Object value) {
        if (value != null) {
            getRedis().set(buildKey(cacheName, key), value);
            getRedis().sadd(buildCacheName(CACHE_NAMES_KEY), cacheName);
        }

    }

    @Override
    public void put(String cacheName, Object key, Object value, int liveSeconds) {
        if (liveSeconds <= 0) {
            put(cacheName, key, value);
        } else {
            if (value != null) {
                getRedis().setex(buildKey(cacheName, key), liveSeconds, value);
                getRedis().sadd(buildCacheName(CACHE_NAMES_KEY), cacheName, liveSeconds);
            }
        }
    }

    @Override
    public void remove(String cacheName, Object key) {
        getRedis().del(buildKey(cacheName, key));
    }

    @Override
    public void removeAll(String cacheName) {
        String[] keys = new String[]{};
        keys = getRedis().keys(buildKey(cacheName, "*")).toArray(keys);
        if (keys != null && keys.length > 0) {
            getRedis().del(keys);
        }
    }

    @Override
    public <T> T get(String cacheName, Object key, IDataLoader dataLoader) {
        Object data = get(cacheName, key);
        if (data == null) {
            data = dataLoader.load();
            put(cacheName, key, data);
        }
        return (T) data;
    }

    @Override
    public <T> T get(String cacheName, Object key, IDataLoader dataLoader, int liveSeconds) {
        if (liveSeconds <= 0) {
            return get(cacheName, key, dataLoader);
        }
        Object data = get(cacheName, key);
        if (data == null) {
            data = dataLoader.load();
            put(cacheName, key, data, liveSeconds);
        }
        return (T) data;
    }

    @Override
    public Integer getTtl(String cacheName, Object key) {
        Long ttl = getRedis().ttl(buildKey(cacheName, key));
        return ttl != null ? ttl.intValue() : null;
    }

    @Override
    public void setTtl(String cacheName, Object key, int seconds) {
        getRedis().expire(buildKey(cacheName, key), seconds);
    }

    @Override
    public void refresh(String cacheName, Object key) {

    }

    @Override
    public void refresh(String cacheName) {

    }

    @Override
    public List getNames() {
        Set set = getRedis().smembers(buildCacheName(CACHE_NAMES_KEY));
        return set == null ? null : new ArrayList(set);
    }

    @Override
    public List getKeys(String cacheName) {
        Set<String> keySet = getRedis().keys(buildKey(cacheName,"*"));
        if (keySet == null || keySet.size() == 0) {
            return new ArrayList();
        }

        List<String> keys = new ArrayList<>(keySet);
        for (int i = 0; i < keys.size(); i++) {
            keys.set(i, keys.get(i).substring(cacheName.length() + 3));
        }
        return keys;
    }


}
