package com.up.habit.expand.swagger.bean;

/**
 * TODO:
 * <p>
 * @author 王剑洪 on 2019/10/25 13:50
 */
public class Tag {
    private String name;
    private String description;

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
