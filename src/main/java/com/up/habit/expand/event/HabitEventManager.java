package com.up.habit.expand.event;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.up.habit.expand.event.anno.Event;
import com.up.habit.expand.route.anno.Ctr;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.ClassKit;
import com.up.habit.kit.StrKit;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/10/21 16:25
 */
public enum HabitEventManager {
    ME;

    private final Map<String, HabitEventListener> listenerMap;

    private final String eventPackageNames;

    HabitEventManager() {
        listenerMap = new ConcurrentHashMap<>();
        eventPackageNames = PropKit.get("app.event");
        initListeners();
    }

    public void initListeners() {
        Set<Class<?>> listenerClass = new HashSet<>();
        if (StrKit.notBlank(eventPackageNames)) {
            String[] packageArray = ArrayKit.toStrArray(eventPackageNames);
            for (String packageName : packageArray) {
                Set<Class<?>> classSet = ClassKit.getClass(packageName, HabitEventListener.class);
                for (Class<?> clazz : classSet) {
                    Event event = clazz.getAnnotation(Event.class);
                    if (event != null) {
                        HabitEventListener listener = (HabitEventListener) ClassKit.newInstance(clazz);
                        listenerMap.put(event.action(), listener);
                        listenerClass.add(clazz);
                    }
                }
            }
        }
    }

    public HabitEventListener get(String action) {
        return listenerMap.get(action);
    }

    public void add(String action, HabitEventListener listener) {
        listenerMap.put(action, listener);
    }

}
