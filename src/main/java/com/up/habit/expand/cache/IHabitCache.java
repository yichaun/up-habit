package com.up.habit.expand.cache;

import com.jfinal.plugin.activerecord.cache.ICache;
import com.jfinal.plugin.ehcache.IDataLoader;

import java.util.List;

/**
 * TODO:缓存
 *
 * @author 王剑洪 on 2020/3/27 1:25
 */
public interface IHabitCache extends ICache {

    /**
     * TODO:
     *
     * @param cacheName
     * @param key
     * @return T
     * @Author 王剑洪 on 2020/3/27 1:40
     **/
    @Override
    <T> T get(String cacheName, Object key);

    /**
     * TODO:
     *
     * @param cacheName
     * @param key
     * @param value
     * @return void
     * @Author 王剑洪 on 2020/3/27 1:40
     **/
    @Override
    void put(String cacheName, Object key, Object value);

    /**
     * TODO:
     *
     * @param cacheName
     * @param key
     * @param value
     * @param liveSeconds
     * @return void
     * @Author 王剑洪 on 2020/3/27 1:40
     **/
    void put(String cacheName, Object key, Object value, int liveSeconds);

    /**
     * TODO:
     *
     * @param cacheName
     * @param key
     * @return void
     * @Author 王剑洪 on 2020/3/27 1:40
     **/
    @Override
    void remove(String cacheName, Object key);

    /**
     * TODO:
     *
     * @param cacheName
     * @return void
     * @Author 王剑洪 on 2020/3/27 1:40
     **/
    @Override
    void removeAll(String cacheName);

    /**
     * TODO:
     *
     * @param cacheName
     * @param key
     * @param dataLoader
     * @return T
     * @Author 王剑洪 on 2020/3/27 1:40
     **/
    <T> T get(String cacheName, Object key, IDataLoader dataLoader);

    /**
     * TODO:
     *
     * @param cacheName
     * @param key
     * @param dataLoader
     * @param liveSeconds
     * @return T
     * @Author 王剑洪 on 2020/3/27 1:40
     **/
    <T> T get(String cacheName, Object key, IDataLoader dataLoader, int liveSeconds);

    /**
     * TODO:
     *
     * @param cacheName
     * @param key
     * @return java.lang.Integer
     * @Author 王剑洪 on 2020/3/27 1:40
     **/
    Integer getTtl(String cacheName, Object key);

    /**
     * TODO:
     *
     * @param cacheName
     * @param key
     * @param seconds
     * @return void
     * @Author 王剑洪 on 2020/3/27 1:40
     **/
    void setTtl(String cacheName, Object key, int seconds);

    /**
     * TODO:
     *
     * @param cacheName
     * @param key
     * @return void
     * @Author 王剑洪 on 2020/3/27 1:41
     **/
    void refresh(String cacheName, Object key);

    /**
     * TODO:
     *
     * @param cacheName
     * @return void
     * @Author 王剑洪 on 2020/3/27 1:41
     **/
    void refresh(String cacheName);

    /**
     * TODO:
     *
     * @param
     * @return java.util.List
     * @Author 王剑洪 on 2020/3/27 1:41
     **/
    List getNames();

    /**
     * TODO:
     *
     * @param cacheName
     * @return java.util.List
     * @Author 王剑洪 on 2020/3/27 1:41
     **/
    List getKeys(String cacheName);


}
