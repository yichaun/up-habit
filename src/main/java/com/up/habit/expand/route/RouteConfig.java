package com.up.habit.expand.route;

import com.up.habit.expand.config.Config;
import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.StrKit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO:路由配置
 *
 * @author 王剑洪 on 2020/3/27 0:31
 */
@Config(prefix = "app.ctr")
public class RouteConfig {
    /**
     * api接口控制器包路径
     */
    private String api;
    /**
     * web控制器包路径
     */
    private String web;
    /**
     * admin接口控制器包路径
     */
    private String admin;
    /**
     * 移除的控制器
     */
    private String without;
    /**
     * 控制器类名后缀
     */
    private String suffix;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getWithout() {
        return without;
    }

    public void setWithout(String without) {
        this.without = without;
    }

    public String getAll() {
        return getAdmin() + "," + getApi() + "," + getWeb();
    }

    public String getSuffix() {
        if (StrKit.isBlank(suffix)) {
            suffix = "Controller,Ctr";
        }
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    public List<String> withoutList() {
        String withoutStr = getWithout();
        if (StrKit.notBlank(withoutStr)) {
            return Arrays.asList(ArrayKit.toStrArray(withoutStr));
        }
        return new ArrayList<>();

    }

    public String[] getSuffixArray() {
        return ArrayKit.toStrArray(getSuffix());
    }
}

