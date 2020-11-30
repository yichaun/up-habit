package com.up.habit.expand.ws.connection;

import com.up.habit.expand.ws.MessageVo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO:连接监听
 *
 * @author 王剑洪 on 2020/8/4 14:30
 */
public interface ConnectListener<T> {

    /**
     * TODO:连接
     *
     * @param token
     * @param vo
     * @param localConnects
     */
    void connect(String token, ConnectionVo vo,ConcurrentHashMap<String,ConnectionVo> localConnects);

    /**
     * TODO:消息处理
     *
     * @param token
     * @param msg
     * @param vo
     * @param localConnects
     */
    void message(String token, String msg,MessageVo vo, ConcurrentHashMap<String,ConnectionVo> localConnects);

    /**
     * TODO:消息处理
     *
     * @param vo
     * @param localConnects
     */
    void broadcast(MessageVo vo, ConcurrentHashMap<String,ConnectionVo> localConnects);

    /**
     * TODO:断开连接
     *
     * @param token
     * @param vo
     */
    void disconnect(String token, ConnectionVo vo);
}
