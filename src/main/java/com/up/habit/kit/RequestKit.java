package com.up.habit.kit;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO:
 * <p>
 * @author 王剑洪 on 2019/11/14 19:38
 */
public class RequestKit {
    /**
     * 获取所有的请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getAllParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> paraNames = request.getParameterNames();
        for (Enumeration<String> e = paraNames; e.hasMoreElements(); ) {
            String thisName = e.nextElement().toString();
            String thisValue = request.getParameter(thisName);
            params.put(thisName, thisValue);
        }
        return params;
    }


    /**
     * 获取所有的头部参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getAllHead(HttpServletRequest request) {
        Map<String, String> head = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        for (Enumeration<String> e = headerNames; e.hasMoreElements(); ) {
            String thisName = e.nextElement().toString();
            String thisValue = request.getHeader(thisName);
            head.put(thisName, thisValue);
        }
        return head;
    }

    public static String getHost(HttpServletRequest request) {
        int port = request.getServerPort();
        String portStr = ":" + port;
        if (request.getScheme().equals("http")) {
            if (port == 80) {
                portStr = "";
            }
        } else if (request.getScheme().equals("https")) {
            if (port == 443) {
                portStr = "";
            }
        }
        String host = request.getScheme() + "://" + request.getServerName() + portStr + request.getContextPath();
        return host;
    }
}
