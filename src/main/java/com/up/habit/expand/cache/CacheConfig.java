package com.up.habit.expand.cache;

import com.up.habit.expand.config.Config;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/27 15:55
 */
@Config(prefix = "cache")
public class CacheConfig {
    public static final String TYPE_EHCACHE = "ehcache";
    public static final String TYPE_REDIS = "redis";

    private String type = TYPE_EHCACHE;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
