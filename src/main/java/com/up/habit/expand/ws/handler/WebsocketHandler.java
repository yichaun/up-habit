package com.up.habit.expand.ws.handler;

import com.jfinal.handler.Handler;
import com.up.habit.kit.StrKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/8/4 14:27
 */
public class WebsocketHandler extends Handler {

    private Pattern filterUrlRegxPattern;

    public WebsocketHandler(String filterUrlRegx) {
        if (StrKit.isBlank(filterUrlRegx)) {
            throw new IllegalArgumentException("The para filterUrlRegx can not be blank.");
        }
        filterUrlRegxPattern = Pattern.compile(filterUrlRegx);
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        if (filterUrlRegxPattern.matcher(target).find()) {
            return;
        }
        next.handle(target, request, response, isHandled);
    }
}
