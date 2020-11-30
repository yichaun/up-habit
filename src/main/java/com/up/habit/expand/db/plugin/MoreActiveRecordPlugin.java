package com.up.habit.expand.db.plugin;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.dialect.Dialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.up.habit.Habit;
import com.up.habit.exception.HabitConfigException;
import com.up.habit.expand.config.Config;
import com.up.habit.expand.db.dialect.HabitDialect;
import com.up.habit.expand.db.dialect.MysqlHabitDialect;
import com.up.habit.expand.db.kit.ActiveRecordKit;
import com.up.habit.kit.ClassKit;
import com.up.habit.kit.StrKit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO：多数据源链接插件
 *
 * @author 王剑洪 on 2020/3/28 16:27
 */
public class MoreActiveRecordPlugin implements IPlugin {
    private volatile boolean isStarted = false;
    private List<Ret> pluginsList = new ArrayList<>();
    private ConcurrentHashMap<String, HabitDialect> habitDialect = new ConcurrentHashMap();
    private List<DbConfig> configList;


    public MoreActiveRecordPlugin() {
        configList = Habit.configList(DbConfig.class);
        for (DbConfig config : configList) {
            if (StrKit.notBlank(config._name)) {
                DruidPlugin druid = buildDruid(config);
                ActiveRecordPlugin arp = null;
                if (DbKit.MAIN_CONFIG_NAME.equals(config._name)) {
                    arp = new ActiveRecordPlugin(druid);
                } else {
                    arp = new ActiveRecordPlugin(config._name, druid);
                }
                arp.setDevMode(Habit.isDev());
                arp.setTransactionLevel(config.getTransactionLevel());
                ActiveRecordKit.addMapping(arp, config.getMap(), config.getMapWithout());
                arp.getEngine().setToClassPathSourceFactory();//sql模板
                arp.setShowSql(false);
                arp.getSqlKit().getEngine().addSharedMethod(new StrKit());
                ActiveRecordKit.addSqlTemplate(arp, config.getSql(),config.getSqlWithout());
                arp.setCache(Habit.getCache());
                if (StrKit.isBlank(config.getDialect())) {
                    arp.setDialect(new MysqlHabitDialect());
                } else {
                    try {
                        Class dialectClazz = ClassKit.loadClass(config.getDialect());
                        Dialect dialect = (Dialect) ClassKit.newInstance(dialectClazz);
                        arp.setDialect(dialect);
                    } catch (Exception e) {
                        throw new HabitConfigException("db." + config._name + ".dialect is error!");
                    }
                }
                pluginsList.add(Ret.create("druid", druid).set("arp", arp));
            }
        }
    }

    private DruidPlugin buildDruid(DbConfig config) {
        DruidPlugin plugin = new DruidPlugin(config.getUrl(), config.getUser(), config.getPassword());
        if (config.getInitialSize() != null) {
            /**初始连接池大小*/
            plugin.setInitialSize(config.getInitialSize());
        }
        if (config.getMinIdle() != null) {
            /**最小空闲连接数*/
            plugin.setMinIdle(config.getMinIdle());
        }
        if (config.getMaxActive() != null) {
            /**最大活跃连接数*/
            plugin.setMaxActive(config.getMaxActive());
        }
        if (config.getMaxWait() != null) {
            /**配置获取连接等待超时的时间*/
            plugin.setMaxWait(config.getMaxWait());
        }
        if (config.getTimeBetweenEvictionRunsMillis() != null) {
            /**配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒*/
            plugin.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEvictionRunsMillis());
        }
        if (config.getMinEvictableIdleTimeMillis() != null) {
            /**配置连接在池中最小生存的时间*/
            plugin.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTimeMillis());
        }
        if (config.getRemoveAbandoned() != null) {
            /**是否打开连接泄露自动检测*/
            plugin.setRemoveAbandoned(config.getRemoveAbandoned());
        }
        if (config.getRemoveAbandonedTimeoutMillis() != null) {
            /**连接长时间没有使用，被认为发生泄露时长*/
            plugin.setRemoveAbandonedTimeoutMillis(config.getRemoveAbandonedTimeoutMillis());
        }
        if (config.getLogAbandoned() != null) {
            /**发生泄露时是否需要输出 log，建议在开启连接泄露检测时开启，方便排错*/
            plugin.setLogAbandoned(config.getLogAbandoned());
        }
        // 配置防火墙加强数据库安全
        WallFilter wallFilter = new WallFilter();
        wallFilter.setDbType(JdbcConstants.MYSQL);
        plugin.addFilter(wallFilter);
        //配置监控
        StatFilter statFilter = new StatFilter();
        statFilter.setMergeSql(true);
        statFilter.setLogSlowSql(true);
        statFilter.setSlowSqlMillis(1500);

        plugin.addFilter(statFilter);

        //是否打印SQL
        if (config.isShowSql()) {
            SqlFilter sqlFilter = new SqlFilter();
            sqlFilter.setStatementSqlPrettyFormat(true);
            sqlFilter.setStatementSqlFormatOption(new SQLUtils.FormatOption());
            plugin.addFilter(sqlFilter);
        }
        return plugin;

    }

    @Override
    public boolean start() {
        if (isStarted) {
            return true;
        }
        for (Ret ret : pluginsList) {
            DruidPlugin druidPlugin = (DruidPlugin) ret.get("druid");
            ActiveRecordPlugin arp = (ActiveRecordPlugin) ret.get("arp");
            druidPlugin.start();
            arp.start();

        }
        isStarted = true;
        return true;
    }

    @Override
    public boolean stop() {
        for (Ret ret : pluginsList) {
            DruidPlugin druidPlugin = (DruidPlugin) ret.get("druid");
            ActiveRecordPlugin arp = (ActiveRecordPlugin) ret.get("arp");
            druidPlugin.stop();
            arp.stop();

        }
        isStarted = false;
        return true;
    }

}
