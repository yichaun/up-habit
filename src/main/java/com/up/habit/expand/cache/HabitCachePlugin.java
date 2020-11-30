package com.up.habit.expand.cache;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.plugin.redis.serializer.FstSerializer;
import com.jfinal.plugin.redis.serializer.ISerializer;
import com.jfinal.plugin.redis.serializer.JdkSerializer;
import com.up.habit.Habit;
import com.up.habit.exception.HabitConfigException;
import com.up.habit.expand.cache.ehcache.HabitEhCache;
import com.up.habit.expand.cache.redis.RedisConfig;
import com.up.habit.kit.ClassKit;
import net.sf.ehcache.CacheManager;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/28 17:12
 */
public class HabitCachePlugin implements IPlugin {

    private static CacheManager cacheManager = null;
    private RedisPlugin redisPlugin = null;

    public HabitCachePlugin() {
        CacheConfig config = Habit.config(CacheConfig.class);
        switch (config.getType()) {
            case CacheConfig.TYPE_REDIS:
                redisPlugin = getRedisPlugin();
            default:
                break;
        }
    }


    public RedisPlugin getRedisPlugin() {
        RedisConfig config = Habit.config(RedisConfig.class);
        if (StrKit.isBlank(config.getCacheName())) {
            throw new RuntimeException("cacheName can not be blank.");
        }
        if (StrKit.isBlank(config.getHost())) {
            throw new RuntimeException("host can not be blank.");
        }
        RedisPlugin redisPlugin;
        if (config.getPort() != null && config.getTimeout() != null && StrKit.notBlank(config.getPassword()) && config.getDatabase() != null && StrKit.notBlank(config.getClientName())) {
            redisPlugin = new RedisPlugin(config.getCacheName(), config.getHost(), config.getPort(), config.getTimeout(), config.getPassword(), config.getDatabase(), config.getClientName());
        } else if (config.getPort() != null && config.getTimeout() != null && StrKit.notBlank(config.getPassword()) && config.getDatabase() != null) {
            redisPlugin = new RedisPlugin(config.getCacheName(), config.getHost(), config.getPort(), config.getTimeout(), config.getPassword(), config.getDatabase());
        } else if (config.getPort() != null && config.getTimeout() != null && StrKit.notBlank(config.getPassword())) {
            redisPlugin = new RedisPlugin(config.getCacheName(), config.getHost(), config.getPort(), config.getTimeout(), config.getPassword());
        } else if (config.getPort() != null && config.getTimeout() != null) {
            redisPlugin = new RedisPlugin(config.getCacheName(), config.getHost(), config.getPort(), config.getTimeout());
        } else if (config.getPort() != null && StrKit.notBlank(config.getPassword())) {
            redisPlugin = new RedisPlugin(config.getCacheName(), config.getHost(), config.getPort(), config.getPassword());
        } else if (StrKit.notBlank(config.getPassword())) {
            redisPlugin = new RedisPlugin(config.getCacheName(), config.getHost(), config.getPassword());
        } else {
            redisPlugin = new RedisPlugin(config.getCacheName(), config.getHost());
        }
         if (StrKit.notBlank(config.getSerializer())) {
            Class<ISerializer> serializerClazz = ClassKit.loadClass(config.getSerializer());
            if (serializerClazz == null) {
                throw new HabitConfigException("redis serializer is error");
            }
            ISerializer serializer = ClassKit.newInstance(serializerClazz);
            redisPlugin.setSerializer(serializer);
        } else {
           Class clazz=  ClassKit.loadClass("org.nustaq.serialization.FSTObjectOutput");
            if (clazz!=null){
                redisPlugin.setSerializer(FstSerializer.me);
            }else {
                redisPlugin.setSerializer(JdkSerializer.me);
            }

        }

        return redisPlugin;
    }

    @Override
    public boolean start() {
        if (cacheManager != null) {
            return true;
        }
        if (redisPlugin != null) {
            return redisPlugin.start();
        }
        cacheManager = HabitEhCache.me().getCacheManager();
        return true;
    }

    @Override
    public boolean stop() {
        if (redisPlugin != null) {
            return redisPlugin.stop();
        }
        if (cacheManager != null) {
            cacheManager.shutdown();
            cacheManager = null;
        }
        return true;
    }
}
