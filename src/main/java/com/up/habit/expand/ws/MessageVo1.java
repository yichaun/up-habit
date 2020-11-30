package com.up.habit.expand.ws;

import com.jfinal.kit.Kv;

import java.io.Serializable;

/**
 * TODO:消息体
 *
 * @author 王剑洪 on 2020/8/4 15:20
 */
public class MessageVo1 extends Kv implements Serializable {
    public static final String KEY_ACTION = "action";
    public static final String KEY_CONTENT = "content";

    public <T> T getContent() {
        return (T) get(KEY_CONTENT);
    }

    public <T> MessageVo1 setContent(T content) {
        set(KEY_CONTENT, content);
        return this;
    }


    public static MessageVo1 create() {
        return new MessageVo1();
    }

    public MessageVo1 setAction(String action) {
        set(KEY_ACTION, action);
        return this;
    }

    public String getAction() {
        return getStr(KEY_ACTION);
    }


}
