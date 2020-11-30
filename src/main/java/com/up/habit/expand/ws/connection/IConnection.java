package com.up.habit.expand.ws.connection;

import com.jfinal.kit.JsonKit;
import com.up.habit.expand.ws.MessageVo;
import com.up.habit.expand.ws.WebSocketManager;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/8/4 14:27
 */
public interface IConnection {


    /**
     * TODO:初始化监听
     *
     * @param chatListener
     */
    void init(ConnectListener chatListener);

    /**
     * TODO:客户端连接
     *
     * @param token
     * @param vo
     */
    void connect(String token, ConnectionVo vo,ConcurrentHashMap<String,ConnectionVo> localConnects);

    /**
     * TODO:获取连接对象
     *
     * @param token
     * @return
     */
    ConnectionVo get(String token);

    /**
     * TODO:客户端断开连接
     *
     * @param token
     * @param session
     */
    void disconnect(String token, Session session);

    /**
     * TODO:本地全部连接客户端
     *
     * @return
     */
    ConcurrentHashMap<String, ConnectionVo> localConnections();

    /**
     * TODO:消息接收
     *
     * @param token
     * @param msg
     */
    void receiveMessage(String token, String msg);

    /**
     * TODO:在OnMessage中判断是否是心跳
     * 从客户端的消息判断是否是ping消息
     *
     * @param message 消息
     * @return 是否是ping消息
     */
    default boolean isPing(String message) {
        return "ping".equals(message);
    }

    /**
     * TODO:返回心跳内容
     *
     * @return 返回的pong消息
     */
    default String pong() {
        return "pong";
    }

    /**
     * TODO:心跳响应
     *
     * @param token
     * @return
     */
    default void heartResponse(String token) {
        ConnectionVo vo = get(token);
        if (vo != null) {
            vo.setLastHeartTime(System.currentTimeMillis());
            send(vo.getSession(), pong());
        }
    }

    /**
     * TODO:发送消息
     *
     * @param session
     * @param message
     * @return
     */
    default boolean send(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * TODO:广播
     *
     * @param vo
     */
    void broadcast(MessageVo vo);

}
