package com.up.habit.expand.swagger.bean;

/**
 * TODO:
 * <p>
 * @author 王剑洪 on 2019/10/25 14:58
 */
public class License {
    private String name;
    private String url;

    public License name(String name) {
        setName(name);
        return this;
    }

    public License url(String url) {
        setUrl(url);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
