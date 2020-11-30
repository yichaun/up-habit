package com.up.habit.app.controller;

import com.up.habit.app.controller.validator.type.IntegerValidate;
import com.up.habit.app.controller.validator.type.PositiveIntegerValidate;
import com.up.habit.expand.route.anno.Api;
import com.up.habit.expand.route.anno.Ctr;
import com.up.habit.expand.route.anno.Param;
import com.up.habit.expand.route.anno.Params;
import com.up.habit.app.service.CommonService;

/**
 * TODO:通用控制器
 *
 * @author 王剑洪 on 2019/12/16 23:49
 */
@Ctr(value = "/common", name = "通用接口")
public class CommonController extends HabitController {

    @Api("获取图片验证码")
    @Params({
            @Param(name = "length", des = "验证码长度", dataType = PositiveIntegerValidate.class, required = false),
            @Param(name = "width", des = "验证码图片宽度", dataType = PositiveIntegerValidate.class, required = false),
            @Param(name = "height", des = "验证码图片高度", dataType = PositiveIntegerValidate.class, required = false)})
    public void captcha() {
        render(CommonService.me.captcha(getInt("length"), getInt("width"), getInt("height")));
    }

}
