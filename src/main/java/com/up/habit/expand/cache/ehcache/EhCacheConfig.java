package com.up.habit.expand.cache.ehcache;

import com.up.habit.expand.config.Config;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/27 1:34
 */
@Config(prefix = "cache.ehcache")
public class EhCacheConfig {
    private String configFileName;

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }
}
