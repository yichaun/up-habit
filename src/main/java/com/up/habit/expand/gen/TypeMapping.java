package com.up.habit.expand.gen;

import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.app.controller.validator.type.*;
import com.up.habit.kit.StrKit;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/6/10 16:56
 */
public class TypeMapping extends com.jfinal.plugin.activerecord.generator.TypeMapping {
    protected Map<String, String> getterTypeMap = new HashMap<String, String>() {{
        this.put("java.lang.String", "getStr");
        this.put("java.lang.Integer", "getInt");
        this.put("java.lang.Long", "getLong");
        this.put("java.lang.Double", "getDouble");
        this.put("java.lang.Float", "getFloat");
        this.put("java.lang.Short", "getShort");
        this.put("java.lang.Byte", "getByte");
    }};

    protected Map<String, String> sqlJavaTypeMap = new HashMap<String, String>() {{
        //https://www.cnblogs.com/hellocz/p/6393500.html
        this.put("char", "java.lang.String");
        this.put("varchar", "java.lang.String");
        this.put("tinytext", "java.lang.String");
        this.put("longtext", "java.lang.String");
        this.put("text", "java.lang.String");
        this.put("mediumtext", "java.lang.String");

        this.put("binary", "java.lang.byte[]");
        this.put("bit", "java.lang.byte[]");
        this.put("blob", "java.lang.byte[]");
        this.put("longblob", "java.lang.byte[]");
        this.put("mediumblob", "java.lang.byte[]");
        this.put("tinyblob", "java.lang.byte[]");
        this.put("varbinary", "java.lang.byte[]");

        this.put("datetime", "java.util.Date");
        this.put("date", "java.util.Date");
        this.put("time", "java.util.Date");
        this.put("timestamp", "java.util.Date");
        this.put("year", "java.util.Date");

        this.put("int", "java.lang.Integer");
        this.put("bool", "java.lang.Integer");
        this.put("boolean", "java.lang.Integer");
        this.put("mediumint", "java.lang.Integer");
        this.put("tinyint", "java.lang.Boolean");

        this.put("decimal", "java.math.BigDecimal");
        this.put("numeric", "java.math.BigDecimal");

        this.put("double", "java.lang.Double");
        this.put("real", "java.lang.Double");

        this.put("bigint", "java.lang.Long");
        this.put("float", "java.lang.Float");
        this.put("smallint", "java.lang.Short");
    }};

    protected Map<String, String> queryTypeMap = new HashMap<String, String>() {{
        this.put("java.lang.String", "like");
        this.put("java.lang.byte[]", "=");
        this.put("java.util.Date", "between");
        this.put("java.lang.Integer", "=");
        this.put("java.math.BigDecimal", "=");
        this.put("java.lang.Double", "=");
        this.put("java.lang.Long", "=");
        this.put("java.lang.Float", "=");
        this.put("java.lang.Short", "=");
    }};

    protected Map<String, String> htmlTypeMap = new HashMap<String, String>() {{

        /*Radio, Checkbox, Input, InputNumber, Select, Switch, DatePicker, DateTimePicker, Upload, RichText*/
        this.put("java.lang.String", "Input");
        this.put("java.lang.byte[]", "Input");
        this.put("java.util.Date", "DateTimePicker");
        this.put("java.lang.Integer", "InputNumber");
        this.put("java.math.BigDecimal", "Input");
        this.put("java.lang.Double", "Input");
        this.put("java.lang.Long", "Input");
        this.put("java.lang.Float", "Input");
        this.put("java.lang.Short", "Input");
        this.put("java.lang.Boolean", "RadioButton");
    }};

    protected Map<String, Class<? extends IHabitValidate>> validateType = new HashMap<String, Class<? extends IHabitValidate>>() {{
        this.put("java.lang.String", StringValidate.class);
        this.put("java.lang.byte[]", StringValidate.class);
        this.put("java.util.Date", DateTimeValidate.class);
        this.put("java.lang.Integer", IntegerValidate.class);
        this.put("java.math.BigDecimal", DecimalValidate.class);
        this.put("java.lang.Double", DecimalValidate.class);
        this.put("java.lang.Long", LongValidate.class);
        this.put("java.lang.Float", DecimalValidate.class);
        this.put("java.lang.Short", StringValidate.class);
        this.put("java.lang.Boolean", SwitchValidate.class);
    }};

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Map<String, String> getMap() {
        return this.map;
    }

    public Map<String, String> getGetterTypeMap() {
        return getterTypeMap;
    }

    public void setGetterTypeMap(Map<String, String> getterTypeMap) {
        this.getterTypeMap = getterTypeMap;
    }

    public Map<String, String> getSqlJavaTypeMap() {
        return sqlJavaTypeMap;
    }

    public void setSqlJavaTypeMap(Map<String, String> sqlJavaTypeMap) {
        this.sqlJavaTypeMap = sqlJavaTypeMap;
    }

    public Map<String, String> getQueryTypeMap() {
        return queryTypeMap;
    }

    public void setQueryTypeMap(Map<String, String> queryTypeMap) {
        this.queryTypeMap = queryTypeMap;
    }

    public Map<String, String> getHtmlTypeMap() {
        return htmlTypeMap;
    }

    public void setHtmlTypeMap(Map<String, String> htmlTypeMap) {
        this.htmlTypeMap = htmlTypeMap;
    }

    public String getGetterType(String typeString) {
        return getterTypeMap.get(typeString);
    }

    public String getSqlJavaType(String typeString) {
        return sqlJavaTypeMap.get(typeString);
    }

    public String getQueryType(String typeString) {
        String type = queryTypeMap.get(typeString);
        return StrKit.isBlank(type) ? "=" : type;
    }

    public String getHtmlType(String typeString) {
        String type = htmlTypeMap.get(typeString);
        return StrKit.isBlank(type) ? "Input" : type;
    }

    public Class<? extends IHabitValidate> getValidateType(String javaType) {
        Class<? extends IHabitValidate> clazz = this.validateType.get(javaType);
        if (clazz == null) {
            return StringValidate.class;
        } else {
            return clazz;
        }
    }


    public void setValidateType(Map<String, Class<? extends IHabitValidate>> validateType) {
        this.validateType = validateType;
    }

}
