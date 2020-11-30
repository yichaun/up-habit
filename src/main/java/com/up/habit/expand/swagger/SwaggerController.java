package com.up.habit.expand.swagger;

import com.jfinal.aop.Clear;
import com.jfinal.kit.Kv;
import com.jfinal.template.Engine;
import com.up.habit.app.config.interceptor.HabitParaInterceptor;
import com.up.habit.app.controller.HabitController;
import com.up.habit.kit.RequestKit;


/**
 * TODO:
 * <p>
 *
 * @author 王剑洪 on 2019/10/24 10:05
 */
@Clear(HabitParaInterceptor.class)
public class SwaggerController extends HabitController {


    public void index() {
        Engine engine = Engine.use().setToClassPathSourceFactory();
        String host = RequestKit.getHost(getRequest());
        String json = host + "/doc/json";
        String assets = host + "/doc/assets";
        String html = engine.getTemplate(SwaggerManager.ASSETS_PATH + "index.html")
                .renderToString(Kv.by("host", host)
                        .set("json", json).set("assets", assets));
        renderHtml(html);
    }

    public void assets() {
        String resource = get("name");
        render(SwaggerManager.me().renderAssets(resource, getResponse()));
    }


    public void json() {
        String doc = SwaggerManager.me().getApiDoc(getRequest(), get(0) != null);
        getResponse().addHeader("Access-Control-Allow-Origin", "*");
        renderText(doc);
    }


}
