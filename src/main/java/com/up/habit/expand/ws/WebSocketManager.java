package com.up.habit.expand.ws;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.up.habit.expand.ws.connection.ConnectListener;
import com.up.habit.expand.ws.connection.MemoryConnection;
import com.up.habit.expand.ws.connection.ConnectionVo;
import com.up.habit.expand.ws.connection.IConnection;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/8/4 14:57
 */
public enum WebSocketManager {
    ME;

    Log log = Log.getLog(WebSocketManager.class);
    /**
     * 默认具备基于内存的
     */
    private IConnection connection = new MemoryConnection();

    private ConnectListener listener;

    public void init(IConnection connection, ConnectListener listener) {
        if (connection != null) {
            this.connection = connection;
        }
        connection.init(listener);
        heartBeatChecker();
    }

    public IConnection getConnection() {
        return this.connection;
    }

    public void heartBeatChecker() {
        /**线程检测时间间隔,默认3秒*/
        int period = PropKit.getInt("ws.period", 3);
        /*时间间隔,默认5秒,超过五秒没有心跳,则断开连接*/
        int span = PropKit.getInt("ws.span", 200) * 1000;
        newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            /**链接检测*/
            Map<String, ConnectionVo> connections = connection.localConnections();
            connections.forEach((token, vo) -> {
                long interval = now - vo.getLastHeartTime();
                if (interval >= span) {
                    connection.disconnect(token, vo.getSession());
                }
            });
        }, 0, period, TimeUnit.SECONDS);
    }

    /**
     * TODO:消息发送
     *
     * @param session
     * @param vo
     * @return
     */
    public boolean sendMessage(Session session, MessageVo vo) {
        return sendMessage(session, JsonKit.toJson(vo));
    }

    /**
     * TODO:消息发送
     *
     * @param session
     * @param message
     * @return
     */
    public boolean sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
            return true;
        } catch (IOException e) {
            log.error("sendMessage", e);
            return false;
        }
    }

    public boolean removeSession(Session session) {
        try {
            session.close();
            return true;
        } catch (IOException e) {
            log.error("removeSession", e);
            return false;
        }
    }

    /**
     * TODO:广播
     *
     * @param action
     * @param message
     * @param <T>
     */
    public <T> void broadcast(String action, T message) {
        connection.localConnections().forEach((token, connection) -> {
            MessageVo<T> vo=new MessageVo();
            vo.setAction(action);
            vo.setContent(message);
            sendMessage(connection.getSession(),vo);
        });
    }


}
