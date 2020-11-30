package com.up.habit.app.controller.validator;

import com.up.habit.app.controller.HabitController;
import com.up.habit.app.controller.render.To;
import com.up.habit.expand.route.anno.Param;
import com.up.habit.kit.StrKit;

import java.io.File;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/26 1:20
 */
public abstract class HabitValidator implements IHabitValidate {


    @Override
    public boolean validate(HabitController controller, Param param) {
        if (type().equals(File.class)) {
            return true;
        }
        String value = controller.getRequest().getParameter(param.name());
        /**默认值设置-不能设置,tomcat环境下无法设置参数默认值
        if (StrKit.notBlank(param.defaultValue())) {
            value = param.defaultValue();
            controller.getRequest().getParameterMap().put(param.name(), new String[]{value});
        }*/
        /**参数是否必须*/
        if (param.required() && StrKit.isBlank(value)) {
            ret(controller, param, "不能为空!");
            return false;
        }
        /**非空则进行格式验证*/
        if (StrKit.notBlank(value)) {
            if (!validateFormat(value, controller, param)) {
                if (controller.getRender() == null) {
                    ret(controller, param, "格式不对!");
                }
                return false;
            }
        }
        return true;
    }

    /**
     * TODO:格式验证
     *
     * @param value
     * @param controller
     * @param param
     * @return boolean
     * @Author 王剑洪 on 2020/5/25 11:41
     **/
    public abstract boolean validateFormat(String value, HabitController controller, Param param);

    /**
     * TODO:错误消息返回
     *
     * @param controller
     * @param param
     * @param msg
     */
    protected void ret(HabitController controller, Param param, String msg) {
        String name = param.des();
        if (StrKit.notBlank(name) && name.contains(",")) {
            name = name.substring(0, name.indexOf(","));
        }
        controller.render(To.fail(name + msg));
    }
}
