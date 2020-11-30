package com.up.habit.exception;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/5/24 16:12
 */
public class HabitException extends RuntimeException {
    public HabitException() {
        super();
    }

    public HabitException(String message) {
        super(message);
    }

    public HabitException(String message, Throwable cause) {
        super(message, cause);
    }

    public HabitException(Throwable cause) {
        super(cause);
    }

    protected HabitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
