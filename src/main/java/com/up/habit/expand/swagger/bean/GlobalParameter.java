package com.up.habit.expand.swagger.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局参数
 */
public class GlobalParameter {
    private static List<Parameter> parameterList = new ArrayList<>();

    public static List<Parameter> getParameterList() {
        return parameterList;
    }

    public static void addPara(Parameter parameter) {
        parameterList.add(parameter);
    }
}
