package com.up.habit.expand.swagger.bean;

import com.jfinal.kit.StrKit;
import com.up.habit.app.controller.validator.IHabitValidate;
import com.up.habit.app.controller.validator.type.ArrayValidate;

/**
 * TODO:
 * <p>
 *
 * @author 王剑洪 on 2019/10/25 14:35
 */
public class Parameter {
    /**
     * @name 参数名
     * @in 参数的来源，必填，取值范围：query、header、path、formData、body
     * @description 参数描述
     * @type 参数类型，取值范围：string、number、integer、boolean、array、file
     * @required 参数是否必须，取值范围：true、false (通过路径传参in="path"时必须为true)
     * @default 参数的默认值
     */
    private String name;
    private String in = "formData";
    private String description;
    private String type;
    private boolean allowMultiple = false;
    private boolean required;
    private String format;
    private String defaultValue;

    public Parameter() {

    }

    public Parameter(String name, String description, IHabitValidate type, boolean required, String defaultValue) {
        this.name = name;
        this.description = description;
        this.required = required;
        this.type = type.type().getSimpleName().toLowerCase().toLowerCase();
        if (type instanceof ArrayValidate) {
            this.allowMultiple = true;
            this.type = "string";
        } else {
            if (this.type.equals("date")) {
                this.type = "string";
            }
        }

        this.format = type.format();
        this.defaultValue = defaultValue;
        this.in = "formData";
        if (StrKit.isBlank(format)) {
            this.format = null;
        }
        if (StrKit.isBlank(defaultValue)) {
            this.defaultValue = null;
        }
    }

    public Parameter(String name, String in, String description, String type, boolean required, String format, String defaultValue) {
        this.name = name;
        this.in = in;
        this.description = description;
        this.required = required;
        this.type = type.toLowerCase();
        this.format = format;
        this.defaultValue = defaultValue;
        if (StrKit.isBlank(format)) {
            this.format = null;
        }
        if (StrKit.isBlank(defaultValue)) {
            this.defaultValue = null;
        }
    }

    public Parameter(String name, String description, String type, boolean required, String format, String defaultValue) {
        this.name = name;
        this.description = description;
        this.required = required;
        this.type = type.toLowerCase();
        this.format = format;
        this.defaultValue = defaultValue;
        this.in = "formData";
        if (StrKit.isBlank(format)) {
            this.format = null;
        }
        if (StrKit.isBlank(defaultValue)) {
            this.defaultValue = null;
        }
    }

    public Parameter(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.required = false;
        this.type = type.toLowerCase();
        this.in = "header";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
