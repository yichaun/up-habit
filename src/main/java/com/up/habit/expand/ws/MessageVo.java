package com.up.habit.expand.ws;

import com.jfinal.kit.Kv;

import java.io.Serializable;
import java.util.Map;

/**
 * TODO:消息体
 *
 * @author 王剑洪 on 2020/8/4 15:20
 */
public class MessageVo<T> implements Serializable {
    private String action;
    private String tag;
    private T content;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
    /*public static final String KEY_ACTION = "action";
    public static final String KEY_CONTENT = "content";

    public <T> T getContent() {
        return (T) get(KEY_CONTENT);
    }

    public <T>MessageVo setContent(T content) {
        set(KEY_CONTENT, content);
        return this;
    }


    public static MessageVo create() {
        return new MessageVo();
    }

    public MessageVo setAction(String action) {
        set(KEY_ACTION, action);
        return this;
    }

    public String getAction() {
        return getStr(KEY_ACTION);
    }
*/

}
