package com.up.habit.expand.route;


import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.log.Log;
import com.up.habit.Habit;
import com.up.habit.expand.route.anno.Api;
import com.up.habit.expand.route.anno.Ctr;
import com.up.habit.expand.route.anno.Directories;
import com.up.habit.expand.route.anno.Directory;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.ClassKit;
import com.up.habit.kit.StrKit;

import java.lang.reflect.Method;
import java.util.*;

/**
 * TODO:路由管理
 *
 * @author 王剑洪 on 2020/3/27 0:29
 */
public class RouteManager {
    private static Log log = Log.getLog(RouteManager.class);
    private static RouteManager me;

    private RouteConfig config = Habit.config(RouteConfig.class);

    public static final String TYPE_ADMIN = "admin";
    public static final String TYPE_API = "api";
    public static final String TYPE_WEB = "web";

    public static RouteManager me() {
        if (me == null) {
            me = new RouteManager();
        }
        return me;
    }

    private RouteManager() {
    }

    public void addToRoutes(Routes routes) {
        addRouters(routes, getAdminClazz(), TYPE_ADMIN);
        addRouters(routes, getClazz(config.getApi()), TYPE_API);
        addRouters(routes, getClazz(config.getWeb()), TYPE_WEB);

    }

    /**
     * TODO:分类添加到路由
     *
     * @param routes
     * @param classes
     * @param type    admin,api,web
     */
    public void addRouters(Routes routes, Set<Class<?>> classes, String type) {
        if (ArrayKit.isNotEmpty(classes)) {
            for (Class<?> clazz : classes) {
                Ctr ctr = clazz.getAnnotation(Ctr.class);
                if (ctr == null || StrKit.isBlank(ctr.name())) {
                    //没有控制器注解,或注解名称为空,则过滤
                    continue;
                }
                String controllerKey = getAction(ctr, clazz, type);
                if (TYPE_ADMIN.equals(type)) {
                    routes.add(controllerKey, (Class<? extends Controller>) clazz);
                } else if (TYPE_API.equals(type)) {
                    routes.add(controllerKey, (Class<? extends Controller>) clazz);
                } else {
                    String viewPath = ctr.view();
                    if (com.jfinal.kit.StrKit.notBlank(viewPath)) {
                        routes.add(controllerKey, (Class<? extends Controller>) clazz, viewPath);
                    } else {
                        routes.add(controllerKey, (Class<? extends Controller>) clazz);
                    }
                }
            }
        }
    }

    /**
     * TODO:获取制定包名路径下的所有非移除的控制器类
     *
     * @return java.util.Set<java.lang.Class < ?>>
     * @Author 王剑洪 on 2020/3/27 0:51
     **/
    public Set<Class<?>> getAdminClazz() {
        return getClazz(config.getAdmin());
    }

    /**
     * TODO:获取制定包名路径下的所有非移除的控制器类
     *
     * @param packageNames
     * @return java.util.Set<java.lang.Class < ?>>
     * @Author 王剑洪 on 2020/3/27 0:51
     **/
    public Set<Class<?>> getClazz(String packageNames) {
        Set<Class<?>> classes = new HashSet<>();
        String[] packageArray = ArrayKit.toStrArray(packageNames);
        List<String> withoutList = config.withoutList();
        for (String packageName : packageArray) {
            packageName = packageName.trim();
            if (StrKit.notBlank(packageName)) {
                Set<Class<?>> controllerSet = ClassKit.getClass(packageName, Controller.class);
                for (Class<?> clazz : controllerSet) {
                    if (!withoutList.contains(clazz.getPackage().getName()) && !withoutList.contains(clazz.getName())) {
                        classes.add(clazz);
                    }
                }
            }
        }
        return classes;
    }

    /**
     * TODO:获取模块路径
     *
     * @param path
     * @param ctrPkg
     * @return java.lang.String
     * @Author 王剑洪 on 2020/7/25 20:18
     **/
    private String getModulePath(String path, String ctrPkg) {
        String modulePath = path;
        String packageName = ctrPkg;
        if (StrKit.isBlank(modulePath)) {
            String[] controllerSuffixArray = config.getSuffixArray();
            for (String suffix : controllerSuffixArray) {
                int index = packageName.indexOf("." + suffix.toLowerCase());
                if (index != -1) {
                    packageName = packageName.substring(index + 1 + suffix.length());
                }
            }
            if (packageName.startsWith(".")){
                packageName=packageName.substring(1);
            }
            String[] temp = ArrayKit.toStrArray(packageName, "\\.");
            /*最多三层*/
            if (temp.length > 2) {
                modulePath = "/" + temp[temp.length - 2] + "/" + temp[temp.length - 1];
            } else {
                modulePath = packageName.replace(".", "/");
            }
        }
        if (!modulePath.startsWith("/")) {
            modulePath = "/" + modulePath;
        }
        if (modulePath.equals("/")) {
            modulePath = modulePath.substring(0, modulePath.length() - 1);
        }
        return modulePath;
    }

    /**
     * TODO:获取控制器路径
     *
     * @param ctrKey
     * @param ctrName
     * @return java.lang.String
     * @Author 王剑洪 on 2020/7/25 20:35
     **/
    public String getControllerKey(String ctrKey, String ctrName) {
        String value = ctrKey;
        if (StrKit.isBlank(value)) {
            value = ctrName;
            String[] controllerSuffixArray = config.getSuffixArray();
            for (String suffix : controllerSuffixArray) {
                if (value.endsWith(suffix)) {
                    value = value.substring(0, value.length() - suffix.length());
                }
            }
            value = StrKit.firstCharToLowerCase(value);
        }
        if (!value.startsWith("/")) {
            value = "/" + value;
        }
        if (value.endsWith("/index")){
            value=value.substring(0,value.length()-6);
        }
        return value;
    }

    /**
     * TODO:路由访问规则,最后一个包名+控制器缩写
     *
     * @param ctr
     * @param clazz
     * @return
     */
    public String getAction(Ctr ctr, Class<?> clazz, String type) {
        Directories directories=clazz.getAnnotation(Directories.class);
        String  modulePath="";
        if (directories!=null){
            Directory[] directoryArray=directories.value();
            for (Directory dire:directoryArray){
                modulePath+=StrKit.isBlank(dire.path())?"":"/"+dire.path();
            }
        }else {
            Directory directory = clazz.getAnnotation(Directory.class);
            modulePath = directory == null ? null : directory.path();
        }
        modulePath = getModulePath(modulePath, clazz.getPackage().getName());
        String ctrKey=ctr.value();
        String controllerKey = getControllerKey(ctrKey, clazz.getSimpleName());
        String value = modulePath + controllerKey;
        if (type.equals(TYPE_ADMIN) || type.equals(TYPE_API)) {
            if (!value.startsWith("/" + type)) {
                value = "/" + type + value;
            }
        }
        return value;
    }
}
