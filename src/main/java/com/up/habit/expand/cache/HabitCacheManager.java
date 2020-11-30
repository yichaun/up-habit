package com.up.habit.expand.cache;

import com.up.habit.Habit;
import com.up.habit.expand.cache.ehcache.HabitEhCache;
import com.up.habit.expand.cache.redis.HabitRedisCache;

/**
 * TODO:缓存管理
 *
 * @author 王剑洪 on 2020/3/27 1:31
 */
public class HabitCacheManager {
    private static HabitCacheManager me = new HabitCacheManager();
    private CacheConfig config = Habit.config(CacheConfig.class);

    private HabitCacheManager() {
    }

    public static HabitCacheManager me() {
        return me;
    }

    public IHabitCache getCache() {
        switch (config.getType()) {
            case CacheConfig.TYPE_REDIS:
                return new HabitRedisCache();
            default:
                return HabitEhCache.me();
        }
    }

}
