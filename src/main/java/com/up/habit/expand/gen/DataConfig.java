package com.up.habit.expand.gen;

import com.up.habit.kit.ArrayKit;
import com.up.habit.kit.StrKit;

import java.util.*;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/6/1 14:52
 */
public class DataConfig {
    private String name;
    private String url;
    private String user;
    private String password;

    private String[] excludedTables;
    private String[] removedTableNamePrefixes;

    private String basePackage;
    private String modelPackage;
    private String srvPackage;
    private String ctrPackage;
    private String table;
    private String vuePath;

    private boolean htmlDict = true;
    private boolean sqlDict = true;
    private boolean model = true;
    private boolean service = true;
    private boolean sql = true;
    private boolean controller = true;
    private boolean vue = true;
    private boolean js = true;

    public void setGenFile(boolean htmlDict, boolean sqlDict, boolean model, boolean service, boolean sql, boolean controller, boolean vue, boolean js) {
        this.htmlDict = htmlDict;
        this.sqlDict = sqlDict;
        this.model = model;
        this.service = service;
        this.sql = sql;
        this.controller = controller;
        this.vue = vue;
        this.js = js;
    }

    public void setOnlyModel() {
        this.htmlDict = true;
        this.sqlDict = true;
        this.model = true;
        this.service = false;
        this.sql = false;
        this.controller = false;
        this.vue = false;
        this.js = false;
    }


    public String getUrl() {
        String[] tmp = url.split("/");
        name = tmp[tmp.length - 1].split("\\?")[0];
        url = url + (url.contains("?") ? "&useSSL=false" : "?useSSL=false") + "&characterEncoding=UTF-8";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getExcludedTables() {
        Set<String> excluded = new HashSet<>();
        if (excludedTables != null) {
            for (String tmp : excludedTables) {
                excluded.add(tmp);
            }
        }
        return excluded;
    }

    public void setExcludedTables(String... excludedTables) {
        this.excludedTables = excludedTables;
    }


    public String[] getRemovedTableNamePrefixes() {
        return removedTableNamePrefixes;
    }

    public void setRemovedTableNamePrefixes(String... removedTableNamePrefixes) {
        this.removedTableNamePrefixes = removedTableNamePrefixes;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getModelPackage() {
        if (StrKit.isBlank(modelPackage)) {
            modelPackage = getBasePackage() + ".model";
        }
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public String getSrvPackage() {
        if (StrKit.isBlank(srvPackage)) {
            srvPackage = getBasePackage() + ".service";
        }
        return srvPackage;
    }

    public void setSrvPackage(String srvPackage) {
        this.srvPackage = srvPackage;
    }

    public String getCtrPackage() {
        if (StrKit.isBlank(ctrPackage)) {
            ctrPackage = getBasePackage() + ".controller.admin";
        }
        return ctrPackage;
    }

    public void setCtrPackage(String ctrPackage) {
        this.ctrPackage = ctrPackage;
    }

    public List<String> getTable() {
        if (StrKit.notBlank(table)) {
            return Arrays.asList(ArrayKit.toStrArray(table));
        }
        return new ArrayList<>();
    }

    public void setTable(String table) {
        this.table = table;
    }

    public boolean isHtmlDict() {
        return htmlDict;
    }

    public void setHtmlDict(boolean htmlDict) {
        this.htmlDict = htmlDict;
    }

    public boolean isSqlDict() {
        return sqlDict;
    }

    public void setSqlDict(boolean sqlDict) {
        this.sqlDict = sqlDict;
    }

    public boolean isModel() {
        return model;
    }

    public void setModel(boolean model) {
        this.model = model;
    }

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public boolean isSql() {
        return sql;
    }

    public void setSql(boolean sql) {
        this.sql = sql;
    }

    public boolean isController() {
        return controller;
    }

    public void setController(boolean controller) {
        this.controller = controller;
    }

    public boolean isVue() {
        return vue;
    }

    public void setVue(boolean vue) {
        this.vue = vue;
    }

    public boolean isJs() {
        return js;
    }

    public void setJs(boolean js) {
        this.js = js;
    }

    public String getVuePath() {
        return vuePath;
    }

    public void setVuePath(String vuePath) {
        this.vuePath = vuePath;
    }
}
