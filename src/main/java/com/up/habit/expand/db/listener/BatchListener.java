package com.up.habit.expand.db.listener;

/**
 * TODO:
 *
 * @author 王剑洪 on 2019/10/13 1:08
 */
public interface BatchListener {
    /**
     * 批量执行sql前
     *
     * @return
     */
    default boolean batchBefore() {
        return true;
    }

    ;

    /**
     * 批量执行sql后
     *
     * @return
     */
    default boolean batchAfter() {
        return true;
    }
}
