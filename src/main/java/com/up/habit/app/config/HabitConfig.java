package com.up.habit.app.config;

import com.jfinal.config.*;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.template.Engine;
import com.up.habit.Habit;
import com.up.habit.app.config.handler.HabitActionReporter;
import com.up.habit.app.config.interceptor.HabitUnifiedInterceptor;
import com.up.habit.app.config.interceptor.HabitParaInterceptor;
import com.up.habit.app.controller.CommonController;
import com.up.habit.expand.cache.HabitCachePlugin;
import com.up.habit.expand.config.HabitConfigManager;
import com.up.habit.expand.db.plugin.MoreActiveRecordPlugin;
import com.up.habit.expand.job.HabitQuartzManager;
import com.up.habit.expand.log.LogbackBuilder;
import com.up.habit.expand.log.LogbackFactory;
import com.up.habit.expand.route.RouteManager;
import com.up.habit.expand.swagger.SwaggerManager;
import com.up.habit.kit.StrKit;

/**
 * TODO:启动配置
 *
 * @author 王剑洪 on 2020/3/26 0:28
 */
public class HabitConfig extends JFinalConfig {
    @Override
    public void configConstant(Constants me) {
        HabitConfigManager.me();
        /**开启对 jfinal web 项目组件 Controller、Interceptor、Validator 的注入*/
        me.setInjectDependency(true);
        /**开启对超类的注入。不开启时可以在超类中通过 Aop.get(...) 进行注入*/
        me.setInjectSuperClass(true);
        me.setDevMode(Habit.isDev());
        me.setLogFactory(new LogbackFactory());
        LogbackBuilder.actionReporter();
        me.setActionReporter(new HabitActionReporter());
    }

    @Override
    public void configRoute(Routes me) {
        me.setMappingSuperClass(true);
        /**通用控制器*/
        me.add("/common", CommonController.class);
        RouteManager.me().addToRoutes(me);
        SwaggerManager.me().addToRoute(me);

    }

    @Override
    public void configEngine(Engine me) {
        me.setDevMode(Habit.isDev());
        me.addSharedMethod(new StrKit());
    }

    @Override
    public void configPlugin(Plugins me) {
        me.add(new MoreActiveRecordPlugin());
        me.add(new HabitCachePlugin());
    }

    @Override
    public void configInterceptor(Interceptors me) {
        /**统一异常拦截器*/
        me.addGlobalActionInterceptor(new HabitUnifiedInterceptor());
        /**参数验证拦截器*/
        me.addGlobalActionInterceptor(new HabitParaInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {
        /**druid数据监控*/
        DruidStatViewHandler dvh = new DruidStatViewHandler("/druid");
        me.add(dvh);
    }

    @Override
    public void onStart() {
        super.onStart();
        HabitQuartzManager.me().start();
        Habit.startLog();
    }

    @Override
    public void onStop() {
        super.onStop();
        HabitQuartzManager.me().shutdown();
    }
}
