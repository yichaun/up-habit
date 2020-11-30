package com.up.habit.expand.event;

import com.up.habit.Habit;

import java.io.Serializable;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/10/21 16:20
 */
public class HabitEvent implements Serializable {
    private final long timestamp;
    private String action;
    private Object data;

    public HabitEvent(String action, Object data) {
        this.timestamp = System.currentTimeMillis();
        this.action = action;
        this.data = data;
    }

    public <T> T getData() {
        return (T) data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAction() {
        return action;
    }


}
