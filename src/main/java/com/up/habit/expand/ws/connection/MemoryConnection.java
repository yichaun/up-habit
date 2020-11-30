package com.up.habit.expand.ws.connection;

import com.google.gson.Gson;
import com.jfinal.kit.JsonKit;
import com.up.habit.expand.ws.MessageVo;
import com.up.habit.expand.ws.WebSocketManager;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO:本地连接
 *
 * @author 王剑洪 on 2020/8/4 14:48
 */
public class MemoryConnection implements IConnection {

    ConnectListener connectListener;

    ConcurrentHashMap<String, ConnectionVo> localConnects = new ConcurrentHashMap<>();

    @Override
    public void init(ConnectListener listener) {
        this.connectListener = listener;
    }

    @Override
    public void connect(String token, ConnectionVo vo,ConcurrentHashMap<String,ConnectionVo> localConnects) {
        localConnections().put(token, vo.setLastHeartTime(System.currentTimeMillis()));
        if (connectListener != null) {
            connectListener.connect(token, vo,localConnects);
        }
    }

    @Override
    public ConnectionVo get(String token) {
        return localConnections().get(token);
    }

    @Override
    public void disconnect(String token, Session session) {
        ConnectionVo vo = localConnections().remove(token);
        WebSocketManager.ME.removeSession(session);
        if (connectListener != null) {
            connectListener.disconnect(token, vo);
        }
    }

    @Override
    public ConcurrentHashMap<String, ConnectionVo> localConnections() {
        return localConnects;
    }

    @Override
    public void receiveMessage(String token, String msg) {
        if (isPing(msg)) {
            heartResponse(token);
        } else {
            if (connectListener != null) {
                try{
                    localConnects.get(token).setLastHeartTime(System.currentTimeMillis());
                    MessageVo vo = new Gson().fromJson(msg, MessageVo.class);
                    connectListener.message(token, msg,vo, localConnections());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void broadcast(MessageVo vo) {
        if (connectListener != null) {
            connectListener.broadcast(vo, localConnections());
        }
    }
}
