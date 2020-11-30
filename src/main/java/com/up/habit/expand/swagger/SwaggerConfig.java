package com.up.habit.expand.swagger;

import com.up.habit.expand.config.Config;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/28 17:50
 */
@Config(prefix = "swagger")
public class SwaggerConfig {
    private boolean open = true;
    private String title = "接口文档";
    private String description = "<table style='font-size:14px; font-style:normal'>" +
            "<tr><td>code</td><td>返回码,0表示成功</td></tr>" +
            "<tr><td>msg</td><td>返回消息</td></tr>" +
            "<tr><td>data</td><td>返回内容体,具体看没个接口下的说明</td></tr>" +
            "</table>";
    private String version = "2.0";

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
