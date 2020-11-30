package com.up.habit.app.config.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.up.habit.Habit;
import com.up.habit.app.config.LocalVariable;
import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.render.To;
import com.up.habit.exception.HabitException;
import com.up.habit.kit.RequestKit;
import com.up.habit.kit.StrKit;

/**
 * TODO:统一拦截器
 *
 * @author 王剑洪 on 2020/3/27 9:23
 */
public class HabitUnifiedInterceptor implements Interceptor {

    private static final Log log = Log.getLog("habit_unified");

    public static final String ATTR_KEY_CONTROLLER = "__controller";
    public static final String ATTR_KEY_METHOD = "__method";
    public static final String ATTR_KEY_ACTION_NAME = "__action";
    public static final String ATTR_KEY_REQUEST_IP = "__ip";
    /**
     * 请求时间
     */
    public static final String ATTR_KEY_REQUEST_TIME = "__requestTime";
    /**
     * 响应时间
     */
    public static final String ATTR_KEY_RESPONSE_TIME = "__responseTime";
    /**
     * 项目根路径
     */
    public static final String ATTR_KEY_BASE_URL = "__base_url";


    @Override
    public void intercept(Invocation inv) {
        HabitController controller = (HabitController) inv.getController();
        LocalVariable.ME.setHost(controller.getRequest());
        controller.set(ATTR_KEY_REQUEST_TIME, System.currentTimeMillis());
        controller.set(ATTR_KEY_CONTROLLER, inv.getController().getClass().getName());
        controller.set(ATTR_KEY_METHOD, inv.getMethod());
        controller.set(ATTR_KEY_ACTION_NAME, inv.getMethodName());
        controller.set(ATTR_KEY_BASE_URL, RequestKit.getHost(controller.getRequest()));
        controller.set(ATTR_KEY_REQUEST_IP, IpKit.getRealIp(controller.getRequest()));
        formatHeader(inv);
        try {
            inv.invoke();
            if (controller.getRender() == null) {
                Object retValue = inv.getReturnValue();
                if (retValue != null) {
                    if (retValue instanceof To) {
                        controller.render((To) retValue);
                    } else if (retValue instanceof Boolean) {
                        boolean ret = (Boolean) retValue;
                        controller.render(To.to(ret));
                    } else {
                        controller.render(To.ok(retValue));
                    }
                }else {
                    controller.render(To.fail("无对应内容"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("system error!", e);
            if (e instanceof HabitException) {
                controller.render(To.fail(e.getMessage()));
            } else {
                if (Habit.isDev()) {
                    controller.render(To.fail().set("ex", e));
                } else {
                    inv.getController().renderJson(To.fail(e.getMessage()));
                }
            }
        } finally {
            LocalVariable.ME.removeHost();
        }
    }

    /**
     * TODO:
     *
     * @param inv
     * @return void
     * @Author 王剑洪 on 2020/7/3 1:04
     **/
    private void formatHeader(Invocation inv) {
        Controller controller = inv.getController();
        String contentType = controller.getRequest().getHeader("Content-Type");
        if (StrKit.notBlank(contentType)) {
            if (contentType.contains("multipart/form-data")) {
                controller.getFile(StrKit.getRandomUUID());
            }
        }
    }
}
