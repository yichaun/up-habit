package com.up.habit.expand.db.plugin;

import com.alibaba.druid.pool.DruidDataSource;
import com.up.habit.Habit;
import com.up.habit.expand.config.Config;
import com.up.habit.expand.db.dialect.HabitDialect;
import com.up.habit.expand.db.dialect.MysqlHabitDialect;
import com.up.habit.kit.ClassKit;
import com.up.habit.kit.StrKit;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/28 16:28
 */
@Config(prefix = "db", array = "db.array")
public class DbConfig {
    public String _name;
    /**
     * 链接地址
     */
    private String url;
    /**
     * 链接用户
     */
    private String user;
    /**
     * 链接密码
     */
    private String password;
    private String map;
    private String sql;
    private String mapWithout;
    private String sqlWithout;

    private boolean showSql = true;
    private int transactionLevel = 2;


    private Integer initialSize;
    private Integer minIdle;
    private Integer maxActive;
    private Integer maxWait;
    private Long timeBetweenEvictionRunsMillis;
    private Long minEvictableIdleTimeMillis;
    private Long timeBetweenConnectErrorMillis;
    private Boolean logAbandoned;
    private Boolean removeAbandoned;
    private Long removeAbandonedTimeoutMillis;

    private String dialect;


    public String getUrl() {
        if (!url.startsWith("jdbc:mysql://")) {
            url = "jdbc:mysql://" + url + "?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getMapWithout() {
        return mapWithout;
    }

    public void setMapWithout(String mapWithout) {
        this.mapWithout = mapWithout;
    }

    public String getSqlWithout() {
        return sqlWithout;
    }

    public void setSqlWithout(String sqlWithout) {
        this.sqlWithout = sqlWithout;
    }

    public boolean isShowSql() {
        return showSql;
    }

    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    public int getTransactionLevel() {
        return transactionLevel;
    }

    public void setTransactionLevel(int transactionLevel) {
        this.transactionLevel = transactionLevel;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Integer getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    public Long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public Long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public Long getTimeBetweenConnectErrorMillis() {
        return timeBetweenConnectErrorMillis;
    }

    public void setTimeBetweenConnectErrorMillis(Long timeBetweenConnectErrorMillis) {
        this.timeBetweenConnectErrorMillis = timeBetweenConnectErrorMillis;
    }

    public Boolean getLogAbandoned() {
        return logAbandoned;
    }

    public void setLogAbandoned(Boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    public Boolean getRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(Boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public Long getRemoveAbandonedTimeoutMillis() {
        return removeAbandonedTimeoutMillis;
    }

    public void setRemoveAbandonedTimeoutMillis(Long removeAbandonedTimeoutMillis) {
        this.removeAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;
    }

    public String getDialect() {
       return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
}
