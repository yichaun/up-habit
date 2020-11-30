package com.up.habit.expand.swagger;

import com.alibaba.fastjson.JSON;
import com.jfinal.config.Routes;
import com.jfinal.core.NotAction;
import com.jfinal.render.Render;
import com.up.habit.Habit;
import com.up.habit.app.config.interceptor.HabitParaInterceptor;
import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.app.controller.validator.type.IntegerValidate;
import com.up.habit.app.controller.validator.type.StringValidate;
import com.up.habit.expand.route.RouteConfig;
import com.up.habit.expand.route.RouteManager;
import com.up.habit.expand.route.anno.Api;
import com.up.habit.expand.route.anno.Ctr;
import com.up.habit.expand.route.anno.Param;
import com.up.habit.expand.route.anno.Params;
import com.up.habit.expand.swagger.bean.*;
import com.up.habit.kit.RequestKit;
import com.up.habit.kit.StrKit;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/28 17:45
 */
public class SwaggerManager {
    public final static String ASSETS_PATH = "/com/up/habit/expand/swagger/assets/";

    public static SwaggerManager me() {
        return new SwaggerManager();
    }

    private SwaggerConfig config = Habit.config(SwaggerConfig.class);


    public static List<Parameter> commonPara = new ArrayList<>();


    public void addCommonParameter(Parameter parameter) {
        commonPara.add(parameter);
    }

    /**
     * TODO:swagger添加到路由
     *
     * @param routes
     */
    public void addToRoute(Routes routes) {
        if (config.isOpen()) {
            routes.add("/doc", SwaggerController.class, "/swagger");
        }
    }

    /**
     * TODO:获取文档信息
     *
     * @param request
     * @param isAdmin
     * @return
     */
    public String getApiDoc(HttpServletRequest request, boolean isAdmin) {
        String host = RequestKit.getHost(request);
        Swagger swagger = getSwagger(request.getContextPath(), isAdmin);
        Info info = new Info(config.getTitle(), config.getDescription(), config.getVersion());
        swagger.setInfo(info);

        List<String> schemes = new ArrayList<>();
        if (host.startsWith("http://")) {
            schemes.add("http");
        } else if (host.startsWith("https://")) {
            schemes.add("https");
        }
        swagger.setSchemes(schemes);
        String json = JSON.toJSONString(swagger);
        json = json.replaceAll("\"defaultValue\"", "\"default\"");
        return json;

    }

    private static Swagger getSwagger(String basePath, boolean isAdmin) {
        Swagger swagger = new Swagger();
        swagger.setBasePath(basePath);
        RouteConfig config = Habit.config(RouteConfig.class);
        Set<Class<?>> classes = RouteManager.me().getClazz(isAdmin ? config.getAdmin() : config.getApi());
        if (classes != null && !classes.isEmpty()) {
            for (Class<?> clazz : classes) {
                Ctr ctr = clazz.getAnnotation(Ctr.class);
                if (ctr == null || StrKit.isBlank(ctr.name())) {
                    continue;
                }
                swagger.addTag(new Tag(ctr.name(), ctr.des()));
                String controllerKey = RouteManager.me().getAction(ctr, clazz, isAdmin ? RouteManager.TYPE_ADMIN : RouteManager.TYPE_API);
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    Api api = method.getAnnotation(Api.class);
                    NotAction notAction = method.getAnnotation(NotAction.class);
                    if (api == null || notAction != null) {
                        continue;
                    }
                    Path path = new Path();
                    String action = controllerKey + "/" + method.getName();
                    Operation operation = new Operation(api.value(), ctr.des(), action);
                    operation.addConsume("application/x-www-form-urlencoded");
                    operation.addTag(ctr.name());
                    operation.addProduce("application/json");
                    //参数
                    Params params = method.getAnnotation(Params.class);
                    //公共参数
                    if (!commonPara.isEmpty()) {
                        for (int i = 0; i < commonPara.size(); i++) {
                            operation.addParameter(commonPara.get(i));
                        }
                    }
                    boolean hasPageNo = false;
                    boolean hasPageSize = false;
                    if (params != null) {
                        Param[] paramArray = params.value();
                        if (paramArray.length > 0) {
                            for (int i = 0; i < paramArray.length; i++) {
                                if ("pageNo".equals(paramArray[i].name())) {
                                    hasPageNo = true;
                                }
                                if ("pageSize".equals(paramArray[i].name())) {
                                    hasPageSize = true;
                                }
                                IHabitValidate validate = HabitParaInterceptor.getValidate(paramArray[i]);
                                String dict = paramArray[i].dict();
                                dict = StrKit.isBlank(dict) ? "" : ",按照字典类型" + dict + "传值";
                                operation.addParameter(new Parameter(paramArray[i].name(), paramArray[i].des() + dict, validate, paramArray[i].required(), paramArray[i].defaultValue()));
                            }
                        }
                    }
                    //分页接口自动添加页码,数量参数
                    if (method.getName().startsWith("page")) {
                        if (!hasPageNo) {
                            operation.addParameter(new Parameter("pageNo", "页码", new IntegerValidate(), false, "1"));
                        }
                        if (!hasPageSize) {
                            operation.addParameter(new Parameter("pageSize", "数量", new IntegerValidate(), false, "20"));
                        }
                    }
                    if (isAdmin) {
                        operation.addParameter(new Parameter("Accept-Token", "header", "用户凭证", "string", false, null, ""));
                    } else {
                        operation.addParameter(new Parameter("token", "登录凭证", new StringValidate(), false, ""));
                    }
                    setResponse(operation, api.response());
                    path.setPost(operation);
                    swagger.addPath(action, path);
                }
            }
        }
        return swagger;
    }

    private static void setResponse(Operation operation, String response) {
        if (StrKit.notBlank(response)) {
            if (response.contains(":-")) {
                String[] itemArray = response.split(":\\|");
                StringBuilder sb = new StringBuilder();
                sb.append("<table style='font-size:14px; font-style:normal'>");
                for (String it : itemArray) {
                    if (StrKit.notBlank(it)) {
                        String[] item = it.split(":-");
                        sb.append("<tr>");
                        sb.append("<td>");
                        sb.append(item[0]);
                        sb.append("</td>");
                        sb.append("<td>");
                        sb.append(item.length > 1 ? item[1] : "");
                        sb.append("</td>");
                        sb.append("</tr>");
                    }
                }
                sb.append("</table>");
                operation.addResponse("data", new Response(sb.toString()));
            }
        } else {
            operation.addResponse("data", new Response(""));
        }

    }

    public static InputStream getAssets(String name) throws FileNotFoundException {
        AssetsSource source = new AssetsSource(ASSETS_PATH + name);
        URL url = source.getUrl();
        return new FileInputStream(new File(url.getFile()));
    }

    public Render renderAssets(String resource, HttpServletResponse response) {
        return new Render() {
            @Override
            public void render() {
                InputStream in = null;
                try {
                    if (Habit.isTomcat) {
                        in = SwaggerManager.class.getResourceAsStream(ASSETS_PATH + resource);
                    } else {
                        AssetsSource source = new AssetsSource(ASSETS_PATH + resource);
                        URL url = source.getUrl();
                        in = new FileInputStream(new File(url.getFile()));
                    }
                    IOUtils.copy(in, response.getOutputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(404);
                }
            }
        };
    }

}
