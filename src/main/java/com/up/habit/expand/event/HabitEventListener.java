package com.up.habit.expand.event;


/**
 * TODO:
 *
 * @author 王剑洪 on 2020/10/21 16:23
 */
public interface HabitEventListener {

    /**
     * TODO:事件处理回调
     *
     * @param event
     * @return
     */
    Object onEvent(HabitEvent event);
}
