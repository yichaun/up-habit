package com.up.habit.kit;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/1/8 0:12
 */
public class IpKit extends com.jfinal.weixin.sdk.kit.IpKit {

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        }
        return "未知";
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        }
        return "127.0.0.1";
    }
}
