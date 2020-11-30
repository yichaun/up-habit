package com.up.habit.expand.ws.connection;

import com.jfinal.kit.Kv;

import javax.websocket.Session;
import java.io.Serializable;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/8/4 14:33
 */
public class ConnectionVo extends Kv implements Serializable {

    public static final String KEY_TOKEN = "_token";
    public static final String KEY_SESSION = "_session";
    public static final String KEY_LAST_HEART = "_last_heart";

    public static ConnectionVo create() {
        return new ConnectionVo();
    }

    public static ConnectionVo create(String token, Session session) {
        return new ConnectionVo().setToken(token).setSession(session);
    }

    public static ConnectionVo create(String token, Session session, long lastHeartTime) {
        return new ConnectionVo().setToken(token).setSession(session).setLastHeartTime(lastHeartTime);
    }


    public ConnectionVo setToken(String token) {
        set(KEY_TOKEN, token);
        return this;
    }

    public String getToken() {
        return getStr(KEY_TOKEN);
    }

    public ConnectionVo setSession(Session session) {
        set(KEY_SESSION, session);
        return this;
    }

    public Session getSession() {
        return getAs(KEY_SESSION);
    }

    public ConnectionVo setLastHeartTime(long lastHeartTime) {
        set(KEY_LAST_HEART, lastHeartTime);
        return this;
    }

    public long getLastHeartTime() {
        Long lastHeartTime = getLong(KEY_LAST_HEART);
        return lastHeartTime == null ? 0 : lastHeartTime.longValue();
    }

}
