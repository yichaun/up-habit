package com.up.habit.expand.ws;

import com.jfinal.log.Log;
import com.up.habit.expand.ws.connection.ConnectionVo;
import com.up.habit.kit.StrKit;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/8/4 14:47
 */
@ServerEndpoint("/ws/{token}")
public class WebSocketController {
    Log log = Log.getLog(WebSocketController.class);

    /**
     * TODO:链接打开
     *
     * @param session
     * @param token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        if (StrKit.notBlank(token)) {
            WebSocketManager.ME.getConnection().connect(token, ConnectionVo.create(token, session, System.currentTimeMillis()), WebSocketManager.ME.getConnection().localConnections());
        } else {
            WebSocketManager.ME.removeSession(session);
        }
    }

    /**
     * TODO:消息接收
     *
     * @param message
     * @param session
     * @param token
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("token") String token) {
        WebSocketManager.ME.getConnection().receiveMessage(token, message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("token") String token) {
        WebSocketManager.ME.getConnection().disconnect(token, session);
    }

    @OnError
    public void onError(Throwable t, @PathParam("token") String token) {
        log.error("error:" + token, t);
        WebSocketManager.ME.getConnection().disconnect(token, null);
    }

}
