package com.up.habit.expand.cache.redis;

import com.up.habit.expand.config.Config;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/27 10:07
 */
@Config(prefix = "cache.redis")
public class RedisConfig {
    private String cacheName = "habit";
    private String host = "127.0.0.1";
    private Integer port = 6379;
    private Integer timeout;
    private String password;
    private Integer database;
    private String clientName;
    private String serializer;

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSerializer() {
        return serializer;
    }

    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }
}
