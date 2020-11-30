package com.up.habit.exception;

/**
 * TODO:配置异常
 *
 * @author 王剑洪 on 2020/5/24 17:15
 */
public class HabitConfigException extends HabitException {


    public HabitConfigException() {
        super();
    }

    public HabitConfigException(String message) {
        super(message);
    }

    public HabitConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public HabitConfigException(Throwable cause) {
        super(cause);
    }

    public HabitConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
