package com.up.habit.app.controller;

import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.up.habit.Habit;
import com.up.habit.app.controller.render.To;
import com.up.habit.expand.db.model.HabitModel;
import com.up.habit.kit.StrKit;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/26 1:07
 */
public class HabitController extends Controller {

    @NotAction
    public String getHeaderToken() {
        return getHeader("Accept-Token");
    }

    @NotAction
    public String getToken() {
        return get("token");
    }


    /**
     * TODO:获取页码
     *
     * @return
     */
    @NotAction
    public int num() {
        return getInt(Habit.PARA_KEY_PAGE_NO, Habit.VALUE_DEF_PARA_PAGE_NO);
    }

    /**
     * TODO:获取数量
     *
     * @return
     */
    @NotAction
    public int size() {
        return getInt(Habit.PARA_KEY_PAGE_SIZE, Habit.VALUE_DEF_PARA_PAGE_SIZE);
    }

    @NotAction
    @Override
    public <T> T getModel(Class<T> modelClass) {
        return getModel(modelClass, "", true);
    }

    @NotAction
    public <T extends HabitModel> T getAllInModel(Class<T> modelClass) {
        return (T) InjectorKit.injectAllInModel(modelClass, getRequest());
    }

    @NotAction
    @Override
    public <T> T getBean(Class<T> beanClass) {
        return super.getBean(beanClass, "", true);
    }

    @NotAction
    public <T> T getBean(String key, Type type) {
        return InjectorKit.injectJsonToBean(key, type, this);
    }

    @NotAction
    public void render(To to) {
        /**跨域处理*/
        getResponse().addHeader("Access-Control-Allow-Origin", "*");
        this.renderJson(to);

    }


    /**
     * TODO:获取String数组参数
     *
     * @param name
     * @return java.lang.String[]
     * @Author 王剑洪 on 2020/1/5 20:45
     **/
    @NotAction
    public String[] getArray(String name) {
        return InjectorKit.injectStrArray(name, getRequest());
    }

    /**
     * TODO:获取Int数组参数
     *
     * @param name
     * @return java.lang.Integer[]
     * @Author 王剑洪 on 2020/1/5 20:45
     **/
    @NotAction
    public Integer[] getIntArray(String name) {
        return InjectorKit.injectIntArray(name, getRequest());
    }

    @NotAction
    public BigDecimal getDecimal(String name){
        BigDecimal value=null;
        String strValue=get(name);
        if (StrKit.notBlank(strValue)){
            value=new BigDecimal(strValue);
        }
        return value;
    }


}
