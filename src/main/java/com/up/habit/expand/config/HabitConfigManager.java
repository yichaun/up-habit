package com.up.habit.expand.config;

import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.up.habit.Habit;
import com.up.habit.Habit;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.ClassKit;
import com.up.habit.kit.FileKit;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO:配置管理类
 *
 * @author 王剑洪 on 2020/3/26 1:35
 */
public class HabitConfigManager {
    private static HabitConfigManager me;

    private Map<String, Object> configCache = new ConcurrentHashMap<>();

    public static HabitConfigManager me() {
        if (me == null) {
            me = new HabitConfigManager();
        }
        return me;
    }

    private HabitConfigManager() {
        init(Habit.DEF_PROP_PATH);
    }

    public void init(String folder) {
        String configPath = new File(this.getClass().getResource("/").getFile()).getPath() + "/";
        if (StrKit.notBlank(folder)) {
            folder = folder + "/";
            configPath = configPath + folder;
        }
        List<String> pathList = FileKit.find(configPath, "properties");
        for (String path : pathList) {
            PropKit.append(folder + path);
        }
    }


    public <T> T get(Class<T> clazz) {
        Config config = clazz.getAnnotation(Config.class);
        if (config == null) {
            return get(clazz, null, null);
        }
        return get(clazz, config.prefix(), config.file());
    }


    public <T> List<T> getList(Class<T> clazz) {
        Config config = clazz.getAnnotation(Config.class);
        if (config == null) {
            return new ArrayList<>();
        }
        String[] array = ArrayKit.toStrArray(PropKit.get(config.array()));
        List<T> list = new ArrayList<>();
        for (String name : array) {
            T t = get(clazz, config.prefix() + "." + name, config.file());
            try {
                Field field = t.getClass().getDeclaredField("_name");
                field.setAccessible(true);
                field.set(t, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.add(t);
        }
        return list;
    }

    /**
     * 获取配置信息，并创建和赋值clazz实例
     *
     * @param clazz  指定的类
     * @param prefix 配置文件前缀
     * @param <T>
     * @return
     */
    public <T> T get(Class<T> clazz, String prefix, String file) {
        Object configObject = configCache.get(clazz.getName() + prefix);
        if (configObject == null) {
            synchronized (clazz) {
                if (configObject == null) {
                    configObject = createConfigObject(clazz, prefix, file);
                    configCache.put(clazz.getName() + prefix, configObject);
                }
            }
        }
        return (T) configObject;
    }


    /**
     * TODO:创建一个新的配置对象（Object）
     *
     * @param clazz
     * @param prefix
     * @param file
     * @param <T>
     * @return
     */
    public <T> T createConfigObject(Class<T> clazz, String prefix, String file) {
        Object configObject = ClassKit.newInstance(clazz);
        List<Method> setMethods = ClassKit.getClassSetMethods(clazz);
        if (setMethods != null) {
            for (Method method : setMethods) {
                if (StrKit.notBlank(file) && getClass().getClassLoader().getResource(file) != null) {
                    PropKit.append(file);
                }
                String key = buildKey(prefix, method);
                String value = PropKit.get(key);
                try {
                    if (StrKit.notBlank(value)) {
                        value = value.trim();
                        if (StrKit.notBlank(value)) {
                            Object val = ClassKit.convert(method.getParameterTypes()[0], value);
                            if (val != null) {
                                method.invoke(configObject, val);
                            }
                        }
                    }
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
        return (T) configObject;
    }

    private String buildKey(String prefix, Method method) {
        String key = StrKit.firstCharToLowerCase(method.getName().substring(3));
        if (StrKit.notBlank(prefix)) {
            key = prefix.trim() + "." + key;
        }
        return key;
    }


}
