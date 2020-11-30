package com.up.habit.exception;

/**
 * TODO:参数验证异常
 *
 * @author 王剑洪 on 2020/5/24 23:02
 */
public class HabitValidateException extends HabitException {
    private int code=-1;

    public HabitValidateException() {
        super();
    }

    public HabitValidateException(String message) {
        super(message);
    }

    public HabitValidateException(int code,String message) {
        super(message);
        this.code=code;
    }

    public HabitValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public HabitValidateException(Throwable cause) {
        super(cause);
    }

    public HabitValidateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
