package com.up.habit.expand.route.perm;

/**
 * TODO:权限枚举
 *
 * @author 王剑洪 on 2020/3/26 0:48
 */
public enum Auth {

    NULL(-1, "无权限"),
    DIRECTOR(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮"),
    DATA(4, "数据");

    private int value;
    private String name;

    private Auth(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
