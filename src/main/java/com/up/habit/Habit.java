package com.up.habit;

import com.jfinal.config.JFinalConfig;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.server.undertow.UndertowServer;
import com.up.habit.exception.HabitConfigException;
import com.up.habit.expand.cache.HabitCacheManager;
import com.up.habit.expand.cache.IHabitCache;
import com.up.habit.expand.config.HabitConfigManager;
import com.up.habit.expand.event.HabitEvent;
import com.up.habit.expand.event.HabitEventListener;
import com.up.habit.expand.event.HabitEventManager;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:habit
 *
 * @author 王剑洪 on 2020/3/26 0:28
 */
public class Habit {

    public static final String VERSION = "1.1.0";


    public static boolean isTomcat = true;

    public static String DEF_PROP_PATH = "";

    public static final String CHART_UTF8 = "utf-8";

    public static final String PARA_KEY_PAGE_NO = "pageNo";
    public static final int VALUE_DEF_PARA_PAGE_NO = 1;
    public static final String PARA_KEY_PAGE_SIZE = "pageSize";
    public static final int VALUE_DEF_PARA_PAGE_SIZE = 20;


    public static final List emptyList = new ArrayList();

    public static void start(Class<? extends JFinalConfig> configClass) {
        start(configClass, 80);
    }

    public static void start(Class<? extends JFinalConfig> configClass, int port) {
        String module = PathKit.getWebRootPath().replace(System.getProperty("user.dir"), "").replace("\\", "");
        String resourcePath = module + "/src/main/webapp, classpath:webapp,habit/src/main/webapp";
        UndertowServer server = UndertowServer.create(configClass);
        server.setResourcePath(resourcePath);
        server.setPort(port);
        server.setDevMode(true);
        server.start();
        isTomcat = false;
    }

    public static String getAppName() {
        return PropKit.get("app.name", "up-habit");
    }

    public static void startLog() {
        String line = "------------------------------------------------------------";
        String proName = getAppName() + " is start!";
        int length = (60 - proName.length()) / 2;
        String out = line.substring(0, length) + proName;
        out += line.substring(0, 60 - out.length());
        String startLog = String.format("\n\n%s\n%s\n%s\n%s\n%s\n%s\n%s\n", line, line, line, out, line, line, line);
        System.out.println(startLog);
    }

    public static boolean isDev() {
        return PropKit.getBoolean("app.dev", true);
    }

    /**
     * 获取配置信息
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T config(Class<T> clazz) {
        return HabitConfigManager.me().get(clazz);
    }

    /**
     * 获取配置信息
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> configList(Class<T> clazz) {
        return HabitConfigManager.me().getList(clazz);
    }


    public static IHabitCache getCache() {
        return HabitCacheManager.me().getCache();
    }

    public static HabitEventListener getEvent(String action){
        HabitEventListener listener=HabitEventManager.ME.get(action);
        if (listener==null){
            throw new HabitConfigException(action+" event 未配置!");
        }
        return listener;
    }

    public static Object onEvent(String action, HabitEvent event){
        HabitEventListener listener=HabitEventManager.ME.get(action);
        if (listener!=null){
            return listener.onEvent(event);
        }else {
            return null;
        }
    }

    public static HabitEventListener getEventListener(String action){
        HabitEventListener listener=HabitEventManager.ME.get(action);
        if (listener==null){
            return null;
        }
        return listener;
    }

}
