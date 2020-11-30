package com.up.habit.app.config.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.expand.route.anno.Param;
import com.up.habit.expand.route.anno.Params;
import com.up.habit.kit.ClassKit;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO:参数验证拦截器
 *
 * @author 王剑洪 on 2020/5/8 9:51
 */
public class HabitParaInterceptor implements Interceptor {

    private final static Map<Class<?>, IHabitValidate> LOCAL_VALIDATE = new ConcurrentHashMap<>();

    @Override
    public void intercept(Invocation inv) {
        HabitController controller = (HabitController) inv.getController();
        Method method = inv.getMethod();
        Params params = method.getAnnotation(Params.class);
        if (params != null) {
            Param[] paramArray = params.value();
            for (Param param : paramArray) {
                IHabitValidate validate = getValidate(param);
                if (!validate.validate(controller, param)) {
                    return;
                }
            }
        }
        inv.invoke();
    }

    /**
     * TODO:获取验证类
     *
     * @param param
     * @return
     */
    public static IHabitValidate getValidate(Param param) {
        IHabitValidate validate = LOCAL_VALIDATE.get(param.dataType());
        if (validate == null) {
            validate = ClassKit.newInstance(param.dataType());
            LOCAL_VALIDATE.put(param.dataType(), validate);
        }
        return validate;
    }

}
