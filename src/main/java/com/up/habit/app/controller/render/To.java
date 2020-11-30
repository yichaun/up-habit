package com.up.habit.app.controller.render;

import com.up.habit.kit.ValidatorKit;

import java.util.HashMap;

/**
 * TODO:返回消息体
 *
 * @author 王剑洪 on 2019/12/16 22:23
 */
public class To extends HashMap<String, Object> {
    /**
     * 状态码
     */
    public static final String KEY_CODE = "code";
    /**
     * 返回消息
     */
    public static final String KEY_MSG = "msg";
    /**
     * 数据对象
     */
    public static final String KEY_DATA = "data";

    /**
     * 成功
     */
    public static final int CODE_OK = 0;
    /**
     * 失败
     */
    public static final int CODE_FAIL = -1;
    /**
     * 系统内部错误
     */
    public static final int CODE_ERROR = -500;
    /**
     * 登录信息过期
     */
    public static final int CODE_LOGIN_OVERDUE = -601;
    /**
     * 登录信息过期
     */
    public static final int CODE_LOGIN_NULL = -602;
    /**
     * 没有权限
     */
    public static final int CODE_NOT_PERMISSION = -603;

    /**
     * 初始化一个新创建的 To 对象，使其表示一个空消息。
     */
    public To() {
    }

    public To set(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 初始化一个新创建的 To 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public To(int code, String msg) {
        super.put(KEY_CODE, code);
        super.put(KEY_MSG, msg);
    }

    /**
     * 初始化一个新创建的 To 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public To(int code, String msg, Object data) {
        super.put(KEY_CODE, code);
        super.put(KEY_MSG, msg);
        if (!ValidatorKit.isNull(data)) {
            super.put(KEY_DATA, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static To ok() {
        return To.ok("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static To ok(Object data) {
        return To.ok("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static To ok(String msg) {
        return To.ok(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static To ok(String msg, Object data) {
        return new To(CODE_OK, msg, data);
    }

    public static To fail() {
        return To.fail("操作失败!");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static To fail(String msg) {
        return To.fail(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static To fail(String msg, Object data) {
        return new To(CODE_FAIL, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static To fail(int code, String msg) {
        return new To(code, msg, null);
    }

    public static To to(boolean is) {
        return new To(is ? CODE_OK : CODE_FAIL, is ? "操作成功!" : "操作失败!");
    }

    public boolean isOk() {
        return get(KEY_CODE) != null && (int) get(KEY_CODE) == 0;
    }


    public Integer getCode() {
        return (int) get(KEY_CODE);
    }

    public Object getData() {
        return get(KEY_DATA);
    }

}
